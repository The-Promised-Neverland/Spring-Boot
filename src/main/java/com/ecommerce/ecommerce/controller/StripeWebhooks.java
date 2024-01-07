package com.ecommerce.ecommerce.controller;

import com.stripe.param.checkout.SessionRetrieveParams;
import com.ecommerce.ecommerce.models.Orders.Requests.orderCreationRequest;
import com.ecommerce.ecommerce.models.Orders.ShippingAddressDTO;
import com.ecommerce.ecommerce.models.Orders.orderItemDTO;
import com.ecommerce.ecommerce.models.Orders.paymentDetailsDTO;
import com.ecommerce.ecommerce.services.OrderServices.orderService;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class StripeWebhooks {

    private String stripeWebhookSecret;
    public StripeWebhooks(@Value("${stripe.secret.webhook}") String stripeWebhookSecret, @Value("${stripe.secret.key}") String stripeSecretKey) {
        Stripe.apiKey=stripeSecretKey;
        this.stripeWebhookSecret=stripeWebhookSecret;
    }
    @Autowired
    private orderService orderService;

    @PostMapping("/webhook")
    public  ResponseEntity<String> stripeWebhookEndpoint(@RequestBody String payload,
                                                         @RequestHeader("Stripe-Signature") String sigHeader) throws StripeException {
        Event event=null;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);
        } catch (JsonSyntaxException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
        } catch (SignatureVerificationException e) {
            // Invalid signature
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Deserialization failed");
        }
        switch (event.getType()) {
            case "checkout.session.completed": {
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
        SessionRetrieveParams params=SessionRetrieveParams.builder()
                                                                    .addExpand("line_items.data.price.product")
                                                                    .addExpand("payment_intent")
                                                           .build();


        Session session = Session.retrieve(sessionID, params, null);

        List<LineItem> lineItems=session.getLineItems().getData();

        String user = session.getClientReferenceId();
        String paymentStatus = session.getPaymentIntentObject().getStatus();
        Date paymentTime = new Date(session.getPaymentIntentObject().getCreated() * 1000);
        String paymentEmail = session.getCustomerDetails().getEmail();
        String paymentId = session.getPaymentIntent();

        double tax_price = 0;
        double shipping_price = 0;

        List<orderItemDTO> orderItems = new ArrayList<>();

        for (LineItem item : lineItems) {
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

        double itemsPrice = Double.parseDouble(session.getMetadata().get("items_price"));
        double totalPrice = session.getAmountTotal() / 100;

        String shippingAddressMetadata = session.getMetadata().get("shipping_address");

        // Remove the enclosing "ShippingAddressDTO()" and split the string by ","
        String[] parts = shippingAddressMetadata.replace("ShippingAddressDTO(", "").replace(")", "").split(",");

            // Initialize variables for the address components
        String address = null;
        String city = null;
        String postalCode = null;
        String country = null;

            // Loop through the parts and extract the components
        for (String part : parts) {
            String[] keyValue = part.trim().split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                switch (key) {
                    case "address":
                        address = value;
                        break;
                    case "city":
                        city = value;
                        break;
                    case "postalCode":
                        postalCode = value;
                        break;
                    case "country":
                        country = value;
                        break;
                }
            }
        }

        // Create a ShippingAddressDTO object with the extracted values
        ShippingAddressDTO shippingAddressDTO = new ShippingAddressDTO(address, city, postalCode, country);

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