package com.ecommerce.ecommerce.controller.publicRoutes;

import com.ecommerce.ecommerce.models.Products.productDTO;
import com.ecommerce.ecommerce.services.ProductServices.productService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class product_controller_PUBLIC {
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
}
