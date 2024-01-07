package com.ecommerce.ecommerce.controller;


import com.ecommerce.ecommerce.exceptions.ExpiredSessionException;
import com.ecommerce.ecommerce.exceptions.UnauthorizedAccessException;
import com.ecommerce.ecommerce.models.Products.Requests.productManageRequest;
import com.ecommerce.ecommerce.models.Products.productDTO;
import com.ecommerce.ecommerce.models.Reviews.Requests.reviewRequest;
import com.ecommerce.ecommerce.models.Users.UserDetails;
import com.ecommerce.ecommerce.services.ProductServices.productService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
public class product_controller {
    @Autowired
    private productService productService;


    /**
     * @GET
     * @Params(Optional) - search,page,size,sort
     * @Response - Get products by pagination/search
     */
    @GetMapping
    public ResponseEntity<Page<productDTO>> getProducts(Pageable pageable, @RequestParam(name = "search", defaultValue = "") String search) {
        Page<productDTO> products=productService.getAllProducts(pageable, search);
        return ResponseEntity.ok(products);
    }

    /**
     * @GET
     * @Params - productID
     * @Response - productDTO for product @productID
     */
    @GetMapping("/{productID}")
    public ResponseEntity<productDTO> getProductDetails(@PathVariable("productID") String productID){
        productDTO product=productService.getProductByID(productID);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }


    /**
     * @PUT
     * @Body - name,price,image,brand,category,countInStock,description
     * @Response - new/updated productDTO
     */
    @PutMapping("/auth/productManager")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> productManager(@RequestBody productManageRequest prdt){
        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(authentication!=null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails){
                String user=((UserDetails) authentication.getPrincipal()).get_id();
                productDTO product=productService.UpdateorCreateProduct(user,prdt);
                return new ResponseEntity<>(product,HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("Unauthorized Access!!!",HttpStatus.UNAUTHORIZED);
            }
        } catch (ExpiredSessionException e) {
            // Handle JWT token expiration exception here
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
        } catch (UnauthorizedAccessException e) {
            // Handle other custom exceptions here if needed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    /**
     * @DELETE
     * @Params - productID
     * @Response - Product Deleted Successfully, 200 ok
     */
    @DeleteMapping("/auth/delete/{productID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable("productID") String productID){
        productService.deleteProduct(productID);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }


    /**
     * @PUT
     * @Params - productID
     * @Body - comment, rating
     * @Response - Review Posted Successfully, 200 ok
     */
    @PutMapping("/auth/createReview/{productID}")
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
        } catch (ExpiredSessionException e) {
            // Handle JWT token expiration exception here
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
        } catch (UnauthorizedAccessException e) {
            // Handle other custom exceptions here if needed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
