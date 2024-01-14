package com.ecommerce.ecommerce.controller.adminRoutes;


import com.ecommerce.ecommerce.models.Products.Requests.productManageRequest;
import com.ecommerce.ecommerce.models.Products.productDTO;
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
@RequestMapping("/api/admin/products")
public class product_controller_ADMIN {
    @Autowired
    private productService productService;


    /**
     * @PUT
     * @Body - name,price,image,brand,category,countInStock,description
     * @Response - new/updated productDTO
     */
    @PutMapping("/manage")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createOrUpdateProduct(@RequestBody productManageRequest prdt){
        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(authentication!=null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails){
                String user=((UserDetails) authentication.getPrincipal()).get_id();
                productDTO product=productService.UpdateorCreateProduct(user,prdt);
                return new ResponseEntity<>(product, HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>("Unauthorized Access!!!",HttpStatus.UNAUTHORIZED);
            }
        } catch (RuntimeException e) {
            // Handle JWT token expiration exception here
            return ResponseEntity.status(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED).build();
        }
    }


    /**
     * @DELETE
     * @Params - productID
     * @Response - Product Deleted Successfully, 200 ok
     */
    @DeleteMapping("/delete/{productID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable("productID") String productID){
        productService.deleteProduct(productID);
        return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);
    }





}
