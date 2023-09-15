package com.ecommerce.ecommerce.services.PaymentServices.Stripe;

import com.ecommerce.ecommerce.models.Orders.Requests.orderCreationRequest;
import com.ecommerce.ecommerce.models.Orders.orderItemDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StripePaymentService {
    SessionCreateParams.LineItem.PriceData createPriceData(orderItemDTO orderItemDTO) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount((long)(orderItemDTO.getPrice()*100))
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(orderItemDTO.getProduct())
                                .build())
                .build();
    }

    // build each product in the stripe checkout page
    SessionCreateParams.LineItem createSessionLineItem(orderItemDTO orderItemDTO) {
        return SessionCreateParams.LineItem.builder()
                // set price for each product
                .setPriceData(createPriceData(orderItemDTO))
                // set quantity for each product
                .setQuantity(Long.parseLong(String.valueOf(orderItemDTO.getQty())))
                .build();
    }

    public String createStripeSession(orderCreationRequest orderCreationRequest) throws StripeException {
        // set the private key
        Stripe.apiKey = "sk_test_51NT22sSJxizCNVXP36xCztSl9zsKGVlIpzuewgsi05mUtE7Ymc3nEKuLlDPJbVdpmOXICqj1UpqJ0NxNXpXuauru002E9SldUc";

        List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<>();

        // for each product compute SessionCreateParams.LineItem
        for (orderItemDTO orderItemDTO : orderCreationRequest.getOrderItems()) {
            sessionItemsList.add(createSessionLineItem(orderItemDTO));
        }

        // build the session param
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(orderCreationRequest.getCancelURL() + "?session_id={CHECKOUT_SESSION_ID}")
                .addAllLineItem(sessionItemsList)
                .setSuccessUrl(orderCreationRequest.getSuccessURL() + "?session_id={CHECKOUT_SESSION_ID}")
                .build();

        return Session.create(params).getUrl();
    }

}
