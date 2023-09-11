package com.ecommerce.ecommerce.controller;


import com.ecommerce.ecommerce.exceptions.ExpiredSessionException;
import com.ecommerce.ecommerce.exceptions.UnauthorizedAccessException;
import com.ecommerce.ecommerce.models.Products.Requests.productCreationRequest;
import com.ecommerce.ecommerce.models.Products.productDTO;
import com.ecommerce.ecommerce.models.Reviews.Requests.reviewRequest;
import com.ecommerce.ecommerce.models.Reviews.reviewDTO;
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


    /**
     * @GET
     * @Params(Optional) - search,page,size,sort
     * @Response - Get products by pagination/search
     */
    @GetMapping
    public ResponseEntity<Page<productDTO>> getProducts(Pageable pageable, @RequestParam(name = "search", defaultValue = "") String search) {
        Page<productDTO> products;
        products = productRepository.PaginationAndSearch(search, pageable);
        return ResponseEntity.ok(products);
    }

    /**
     * @GET
     * @Params - productID
     * @Response - productDTO for product @productID
     */
    @GetMapping("/{productID}")
    public ResponseEntity<productDTO> getProductDetails(@PathVariable("productID") String productID){
        productDTO product=productRepository.productDetailsByID(productID);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * @PUT
     * @Body - name,price,image,brand,category,countInStock,description
     * @Response - new/updated productDTO
     */
    @PutMapping("/auth/productManager")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> productManager(@RequestBody productCreationRequest createPrdt){
        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            if(authentication!=null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof customUserDetails){
                String user=((customUserDetails) authentication.getPrincipal()).get_id();

                productDTO newPrdt= new productDTO();
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

    /**
     * @DELETE
     * @Params - productID
     * @Response - Product Deleted Successfully, 200 ok
     */
    @DeleteMapping("/auth/delete/{productID}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable("productID") String productID){
        productRepository.deleteById(productID);
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
            if(authentication!=null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof customUserDetails){
                String user=((customUserDetails) authentication.getPrincipal()).get_id();
                String name=((customUserDetails) authentication.getPrincipal()).getName();

                productDTO product = productRepository.findById(productID).get();
                // Check if the user has already reviewed the product
                boolean hasReviewed = product.getReviews().stream()
                        .anyMatch(review -> review.getUser().equals(user));

                System.out.println(hasReviewed);
                if (hasReviewed) {
                    return new ResponseEntity<>("Product already reviewed",HttpStatus.BAD_REQUEST);
                }

                reviewDTO newReview=new reviewDTO();
                newReview.setUser(user);
                newReview.setName(name);
                newReview.setComment(request.getComment());
                newReview.setRating(request.getRating());

                product.getReviews().add(newReview);
                productRepository.save(product);

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
