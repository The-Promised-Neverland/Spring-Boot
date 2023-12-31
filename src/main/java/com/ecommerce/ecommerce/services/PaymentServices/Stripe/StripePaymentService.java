package com.ecommerce.ecommerce.services.PaymentServices.Stripe;

import com.ecommerce.ecommerce.models.Orders.Requests.orderCreationRequest;
import com.ecommerce.ecommerce.models.Orders.orderItemDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StripePaymentService {

    //Initiate Stripe
    public StripePaymentService(@Value("${stripe.secret.key}") String stripeSecretKey) {
        Stripe.apiKey=stripeSecretKey;
    }

    SessionCreateParams.LineItem.PriceData createPriceData(orderItemDTO orderItemDTO) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount((long)(orderItemDTO.getPrice()*100))
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(orderItemDTO.getName())
                                .putMetadata("product_id",orderItemDTO.getProduct())
                                .putMetadata("product_image",orderItemDTO.getImage())
                                .build())
                .build();
    }


    // build each product in the stripe checkout page
    SessionCreateParams.LineItem createSessionLineItem(orderItemDTO orderItemDTO) {
        return SessionCreateParams.LineItem.builder()
                // set price for each product
                .setPriceData(createPriceData(orderItemDTO))
                // set quantity for each product
                .setQuantity((long) orderItemDTO.getQty())
                .build();
    }

    public String createStripeSession(String user, orderCreationRequest orderCreationRequest) throws StripeException {
        List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<>();

        // for each product compute SessionCreateParams.LineItem
        for (orderItemDTO orderItemDTO : orderCreationRequest.getOrderItems()) {
            sessionItemsList.add(createSessionLineItem(orderItemDTO));
        }


        SessionCreateParams.LineItem taxItem = SessionCreateParams.LineItem.builder()
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount((long) (orderCreationRequest.getTaxPrice() * 100)) // Set the tax price
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Tax")
                                                .build())
                                .build())
                .setQuantity(1L) // You can adjust the quantity if needed
                .build();
        sessionItemsList.add(taxItem);

        // Add Shipping item
        SessionCreateParams.LineItem shippingItem = SessionCreateParams.LineItem.builder()
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount((long) (orderCreationRequest.getShippingPrice() * 100)) // Set the shipping price
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Shipping")
                                                .build())
                                .build())
                .setQuantity(1L) // You can adjust the quantity if needed
                .build();
        sessionItemsList.add(shippingItem);

        // build the session param
        SessionCreateParams params = SessionCreateParams.builder()
                .setClientReferenceId(user)
                .putMetadata("shipping_address",orderCreationRequest.getShippingAddress().toString())
                .putMetadata("items_price", String.valueOf(orderCreationRequest.getItemsPrice()))
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(orderCreationRequest.getCancelURL() + "?session_id={CHECKOUT_SESSION_ID}")
                .addAllLineItem(sessionItemsList)
                .setSuccessUrl(orderCreationRequest.getSuccessURL() + "?session_id={CHECKOUT_SESSION_ID}")
                .build();

        return Session.create(params).getUrl();
    }
}
