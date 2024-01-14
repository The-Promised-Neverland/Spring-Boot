package com.ecommerce.ecommerce.controller.userRoutes;

import com.ecommerce.ecommerce.models.Orders.Requests.orderCreationRequest;
import com.ecommerce.ecommerce.models.Users.UserDetails;
import com.ecommerce.ecommerce.services.PaymentServices.Stripe.StripePaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/paylink")
public class stripePaymentLink_controller_USER {
    @Autowired
    private StripePaymentService stripePaymentService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<String> createSession(@RequestBody orderCreationRequest orderCreationRequest) throws StripeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = ((UserDetails) authentication.getPrincipal()).get_id();
        String session=stripePaymentService.createStripeSession(user,orderCreationRequest);
        return new ResponseEntity<>(session, HttpStatus.CREATED);
    }
}
