package com.ecommerce.ecommerce.services.ProductServices;

import com.ecommerce.ecommerce.models.Products.Requests.productManageRequest;
import com.ecommerce.ecommerce.models.Products.productDTO;
import com.ecommerce.ecommerce.models.Reviews.Requests.reviewRequest;
import com.ecommerce.ecommerce.models.Reviews.reviewDTO;
import com.ecommerce.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class productService {

    @Autowired
    private ProductRepository productRepository;

    public Page<productDTO> getAllProducts(Pageable pageable, String search){
        Page<productDTO> products=productRepository.PaginationAndSearch(search, pageable);
        return products;
    }

    public productDTO getProductByID(String productID){
        productDTO product=productRepository.findById(productID).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Product not found"));
        return product;
    }

    public productDTO UpdateorCreateProduct(String userID, productManageRequest prdt){
        productDTO newPrdt= new productDTO();
        newPrdt.setUser(userID);
        newPrdt.setName(prdt.getName());
        newPrdt.setPrice(prdt.getPrice());
        newPrdt.setImage(prdt.getImage());
        newPrdt.setBrand(prdt.getBrand());
        newPrdt.setCategory(prdt.getCategory());
        newPrdt.setCountInStock(prdt.getCountInStock());
        newPrdt.setDescription(prdt.getDescription());
        productRepository.save(newPrdt);
        return newPrdt;
    }

    public void deleteProduct(String productID){
        productRepository.deleteById(productID);
    }

    public Boolean postProductReview(String productID, String userID, String name, reviewRequest request){
        productDTO product = productRepository.findById(productID).get();

        // Check if the user has already reviewed the product
        boolean hasReviewed = product.getReviews().stream()
                .anyMatch(review -> review.getUser().equals(userID));

        if (hasReviewed) {
            return false;
        }

        reviewDTO newReview=new reviewDTO();
        newReview.setUser(userID);
        newReview.setName(name);
        newReview.setComment(request.getComment());
        newReview.setRating(request.getRating());

        product.getReviews().add(newReview);
        productRepository.save(product);
        return true;
    }
}
