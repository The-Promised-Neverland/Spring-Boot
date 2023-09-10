package com.ecommerce.ecommerce.controller;


import com.ecommerce.ecommerce.exceptions.ExpiredSessionException;
import com.ecommerce.ecommerce.exceptions.UnauthorizedAccessException;
import com.ecommerce.ecommerce.models.Products.Requests.productCreationRequest;
import com.ecommerce.ecommerce.models.Products.productDTO;
import com.ecommerce.ecommerce.models.Users.customUserDetails;
import com.ecommerce.ecommerce.repository.ProductRepository;
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
    private ProductRepository productRepository;


    // Included pagination + search functionality
    // URL - {{DOMAIN}}/api/products?search={keyword}&size={No of items per page}&page={pageNo}&sort={Field to sort},{asec/desc}
    @GetMapping
    public ResponseEntity<Page<productDTO>> getProducts(Pageable pageable, @RequestParam(name = "search", defaultValue = "") String search) {
        Page<productDTO> products;
        products = productRepository.PaginationAndSearch(search, pageable);
        return ResponseEntity.ok(products);
    }


    @GetMapping("/{productID}")
    public ResponseEntity<productDTO> getProductDetails(@PathVariable("productID") String productID){
        productDTO product=productRepository.productDetailsByID(productID);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping("/auth/createProduct")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createProduct(@RequestBody productCreationRequest createPrdt){
        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(authentication!=null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof customUserDetails){
                String user=((customUserDetails) authentication.getPrincipal()).get_id();

                productDTO newPrdt=new productDTO();
                newPrdt.setUser(user);
                newPrdt.setName(createPrdt.getName());
                newPrdt.setPrice(createPrdt.getPrice());
                newPrdt.setImage(createPrdt.getImage());
                newPrdt.setBrand(createPrdt.getBrand());
                newPrdt.setCategory(createPrdt.getCategory());
                newPrdt.setCountInStock(createPrdt.getCountInStock());
                newPrdt.setDescription(createPrdt.getDescription());

                productRepository.save(newPrdt);
                return new ResponseEntity<>(newPrdt,HttpStatus.CREATED);
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
