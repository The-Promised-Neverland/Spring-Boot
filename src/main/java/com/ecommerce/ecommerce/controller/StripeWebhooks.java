package com.ecommerce.ecommerce.controller;

import com.stripe.param.checkout.SessionRetrieveParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ecommerce.ecommerce.models.Orders.Requests.orderCreationRequest;
import com.ecommerce.ecommerce.models.Orders.ShippingAddressDTO;
import com.ecommerce.ecommerce.models.Orders.orderItemDTO;
import com.ecommerce.ecommerce.models.Orders.paymentDetailsDTO;
import com.ecommerce.ecommerce.services.OrderServices.orderService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class StripeWebhooks {
    private static final Logger logger = LoggerFactory.getLogger(StripeWebhooks.class);
    @Autowired
    private orderService orderService;
    private String stripeWebhookSecret="whsec_qzXPPRzVri33WvCP1lOMbIi8UHAsyVww";

    @PostMapping("/webhook")
    public  ResponseEntity<String> stripeWebhookEndpoint(@RequestBody String payload,
                                                         @RequestHeader("Stripe-Signature") String sigHeader) throws StripeException {
        Stripe.apiKey = "sk_test_51NT22sSJxizCNVXP36xCztSl9zsKGVlIpzuewgsi05mUtE7Ymc3nEKuLlDPJbVdpmOXICqj1UpqJ0NxNXpXuauru002E9SldUc";
        logger.warn("STRIPE " + Stripe.API_VERSION);
        Event event=null;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);
            logger.warn("EVENT " + event.getApiVersion());
        } catch (JsonSyntaxException e) {
            logger.warn("Invalid payload"); // Use logger.info for informational message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
        } catch (SignatureVerificationException e) {
            // Invalid signature
            logger.warn("Invalid signature"); // Use logger.info for informational message
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
            // instructions on how to handle this case, or return an error here.
            logger.warn("Deserialization failed"); // Use logger.info for informational message

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Deserialization failed");
        }
        logger.warn("ENTERING THE CASES"); // Use logger.info for informational message
        switch (event.getType()) {
            case "checkout.session.completed": {
                logger.info("checkout.session.completed"); // Use logger.info for informational messages
                logger.info("BEFORE GOING TO HANDLECHECKOUT, PRINTING STRIPE OBJECT", stripeObject.PRETTY_PRINT_GSON);
                handleCheckoutSessionCompleted(stripeObject);
                break;
            }
            default:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No event matched");
        }
        return ResponseEntity.status(HttpStatus.OK).body("No event");
    }

    private void handleCheckoutSessionCompleted(StripeObject stripeObject) throws StripeException {
        Session sessionObj= (Session) stripeObject;
        String sessionID=sessionObj.getId();
        logger.warn("SessionID: " + sessionID); // Use logger.info for informational message

        SessionRetrieveParams params=SessionRetrieveParams.builder()
                                                                    .addExpand("line_items")
                                                                    .addExpand("payment_intent")
                                                           .build();

        Session session = Session.retrieve(sessionID, params, null);
        logger.warn("Session: " + session.toJson()); // Use logger.info for informational message

        String paymentIntentID=session.getPaymentIntent();
        logger.warn("paymentIntentID: " + paymentIntentID); // Use logger.info for informational message


        LineItemCollection lineItems=session.listLineItems().;
        logger.info("LINE ITEMS: ", LineItemCollection.PRETTY_PRINT_GSON.toString());

        String user = session.getClientReferenceId();
        String paymentStatus = session.getPaymentIntentObject().getStatus();
        Date paymentTime = new Date(session.getPaymentIntentObject().getCreated() * 1000);
        String paymentEmail = session.getCustomerDetails().getEmail();
        String paymentId = session.getPaymentIntentObject().getId();



        double tax_price = 0;
        double shipping_price = 0;

        List<orderItemDTO> orderItems = new ArrayList<>();

        for (LineItem item : lineItems.getData()) {
            if ("Tax".equals(item.getDescription())) {
                tax_price = item.getAmountTotal() / 100.0;
            } else if ("Shipping".equals(item.getDescription())) {
                shipping_price = item.getAmountTotal() / 100.0;
            } else {
                // Create an OrderItem object for each non-tax and non-shipping item
                orderItemDTO orderItem = new orderItemDTO();
                orderItem.setName(item.getDescription());
                orderItem.setPrice(item.getAmountTotal() / 100.0);
                orderItem.setQty(Math.toIntExact(item.getQuantity()));
                // Assuming you have a method to retrieve the product metadata
                orderItem.setProduct(item.getPrice().getProductObject().getMetadata().get("product_id"));
                orderItem.setImage(item.getPrice().getProductObject().getMetadata().get("product_image"));
                orderItems.add(orderItem);
            }
        }

        // Remove the filtered-out items (tax and shipping) from the orderItems list
        orderItems.removeIf(item -> item.getName().equals("Tax") || item.getName().equals("Shipping"));

        double itemsPrice = Double.parseDouble(session.getMetadata().get("items_price"));
        double totalPrice = Double.parseDouble(session.getMetadata().get("amount_total")) / 100.0;

        Gson gson = new Gson();
        ShippingAddressDTO shippingAddressDTO = gson.fromJson(session.getMetadata().get("shipping_address"), ShippingAddressDTO.class);

        paymentDetailsDTO paymentDetailsDTO=new paymentDetailsDTO(paymentId,paymentStatus,paymentTime,paymentEmail);

        orderCreationRequest orderCreationRequest=new orderCreationRequest();
        orderCreationRequest.setOrderItems(orderItems);
        orderCreationRequest.setShippingAddress(shippingAddressDTO);
        orderCreationRequest.setPaymentMethod("Stripe");
        orderCreationRequest.setPaymentResult(paymentDetailsDTO);
        orderCreationRequest.setItemsPrice(itemsPrice);
        orderCreationRequest.setTaxPrice(tax_price);
        orderCreationRequest.setShippingPrice(shipping_price);
        orderCreationRequest.setTotalPrice(totalPrice);

        orderService.createOrder(user,orderCreationRequest);
    }
}