package com.ecommerce.ecommerce.controller.userRoutes;

import com.ecommerce.ecommerce.models.Reviews.Requests.reviewRequest;
import com.ecommerce.ecommerce.models.Users.UserDetails;
import com.ecommerce.ecommerce.services.ProductServices.productService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth/products")
public class product_controller_USER {
    @Autowired
    private productService productService;

    /**
     * @PUT
     * @Params - productID
     * @Body - comment, rating
     * @Response - Review Posted Successfully, 200 ok
     */
    @PutMapping("/createReview/{productID}")
    @PreAuthorize("hasAuthority('USER')") // ONLY USERS CAN CREATE REVIEWS
    public ResponseEntity<?> createReview(@PathVariable("productID") String productID , @RequestBody reviewRequest request){
        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(authentication!=null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails){
                String user=((UserDetails) authentication.getPrincipal()).get_id();
                String name=((UserDetails) authentication.getPrincipal()).getName();
                Boolean isPosted=productService.postProductReview(productID,user,name,request);

                if (isPosted==false) {
                    return new ResponseEntity<>("Product already reviewed",HttpStatus.BAD_REQUEST);
                }

                return new ResponseEntity<>("Review posted successfully!!",HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("Unauthorized Access!!!",HttpStatus.UNAUTHORIZED);
            }
        } catch (RuntimeException e) {
            // Handle JWT token expiration exception here
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
        }
    }
}
