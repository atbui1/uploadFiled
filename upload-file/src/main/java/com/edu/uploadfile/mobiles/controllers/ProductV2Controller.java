package com.edu.uploadfile.mobiles.controllers;


import com.edu.uploadfile.mobiles.models.Product;
import com.edu.uploadfile.mobiles.payloads.requests.ProductRequest;
import com.edu.uploadfile.mobiles.payloads.response.ResponseObject;
import com.edu.uploadfile.mobiles.services.serviceImpl.ProductServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8082")
@RestController
@RequestMapping(path = "/api/v2")
public class ProductV2Controller {

    @Autowired
    ProductServiceImp productServiceImp;

    @GetMapping("/products")
    ResponseEntity<?> getAllProduct() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "show all products success", productServiceImp.getAllProduct())
        );
    }
    @GetMapping("/product/dto")
    ResponseEntity<?> getAllProductDto() {
        List<ProductRequest> productRequests = productServiceImp.getAllProductRequest();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "show all products dto success", productRequests)
        );
    }

    /** search product by id */
    @GetMapping("/product/{id}")
    ResponseEntity<?> getProductById(@PathVariable Long id) {
        Product product = productServiceImp.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "show product's id: " + id, product)
        );
    }

    /** upsert product by request body */
    @PostMapping("/product/non-form")
    ResponseEntity<?> upsertProductNoImage(@RequestBody ProductRequest productRequest) {
        Product product = productServiceImp.upsertProductNoImageNoForm(productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "upsert product success raw json", product)
        );
    }

    /** upsert product by request form-data */
    @PostMapping("/product/form-data")
    ResponseEntity<?> upsertProductNoImageFormData(@Valid ProductRequest productRequest) {
        Product product = productServiceImp.upsertProductNoImageFormData(productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "upsert product success form-data", product)
        );
    }

    /** upsert product by form-data with image */
    @PostMapping("/product/form-data/image")
    ResponseEntity<?> upsertProductWithImageNoForm(ProductRequest productRequest) {
        System.out.println("56 11111111111111111111111111111111 controller product v2");
        Product product = productServiceImp.upsertProductWithImageNoForm(productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "upsert product success form-data with image", product)
        );
    }

    /** upsert product by form-data with images */
    @PostMapping("/product/form-data/image/all")
    @Transactional
    ResponseEntity<?> upsertProductWithImagesNoForm(ProductRequest productRequest) {
        System.out.println("80 11111111111111111111111111111111 controller product v2");
        Product product = productServiceImp.upsertProductWithImages(productRequest);
//        Product product = productServiceImp.upsertProductWithImagesNoForm(productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "upsert product success form-data with images all", product)
        );
    }
}
