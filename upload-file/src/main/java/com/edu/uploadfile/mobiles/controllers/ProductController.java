package com.edu.uploadfile.mobiles.controllers;

import com.edu.uploadfile.mobiles.models.Category;
import com.edu.uploadfile.mobiles.models.Product;
import com.edu.uploadfile.mobiles.payloads.requests.ProductRequest;
import com.edu.uploadfile.mobiles.payloads.requests.SignupRequest;
import com.edu.uploadfile.mobiles.payloads.response.ResponseObject;
import com.edu.uploadfile.mobiles.repositories.CategoryRepository;
import com.edu.uploadfile.mobiles.repositories.ImageRepository;
import com.edu.uploadfile.mobiles.repositories.ProductRepository;
import com.edu.uploadfile.mobiles.services.iservice.IStorageService;
import com.edu.uploadfile.mobiles.services.serviceImpl.ImageStorageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://localhost:8082")
@RestController
@RequestMapping(path = "/api/v1")
public class ProductController {

    public static final String baseURL = "/api/v1";
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ImageRepository imageRepository;

//    @Autowired
//    ImageStorageService imageStorageService;
    @Autowired
    IStorageService iStorageService;


    /** get all product */
    @GetMapping("/products")
    public ResponseEntity<ResponseObject> getAllProduct() {
        List<Product> productList = productRepository.findAll();
//        if (productRepository.findAll().size() > 0) {
        if (productList.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "get all product success", productRepository.findAll())
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "not found product", "")
        );
    }

    /** get product by product id */
    @GetMapping("/product/{id}")
    public ResponseEntity<ResponseObject> getProductById(@PathVariable(name = "id") Long productId) {
        Optional product = productRepository.findById(productId);
        if (!product.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "not found product's id: " + productId, "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("failed", "found success product's id: " + productId, product)
        );
    }

    /**
     * many to one
     * add new product
     * get product by category id
     * NO IMAGE
     * http://localhost:8082/api/v1/product/category/{categoryId}/product
     */
    @PostMapping("/product/category/{categoryId}/product")
    public ResponseEntity<ResponseObject> addNewProduct(
            @PathVariable(name = "categoryId") Long categoryId,
            @RequestBody ProductRequest productRequest) {

        if (productRepository.findByName(productRequest.getName().trim()).size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "exits product's name: " + productRequest.getName(), "")
            );
        }
        if (productRequest.getName().trim().equals("")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "product's name not null ", "")
            );
        }

        Optional<Category> category = categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "not found category's id: " + categoryId, "")
            );
        }

        Category category1 = category.get();
        Product product = new Product(
                productRequest.getName().trim(),
                productRequest.getDescription().trim(),
                productRequest.isPublished(),
                category1
        );


        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("failed", "add new product success ", productRepository.save(product))
        );
    }

    /**
     * many to one
     * add new product
     * get product by category id
     * WITH IMAGE
     * from raw --> into form-data
     * http://localhost:8082/api/v1/productImage/category/{categoryId}/product
     */
    @PostMapping("/productImage/category/{categoryId}/product")
    public ResponseEntity<ResponseObject> addNewProductImage(
            @PathVariable(name = "categoryId") Long categoryId,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam boolean published
    ) {

        if (productRepository.findByName(name.trim()).size() > 0) {
            System.out.println("120 nnnnnnnnnnnnnnnnnnn name exits");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "exits product's name: " + name, "")
            );
        }
        if (name.trim().equals("")) {
            System.out.println("126 nnnnnnnnnnnnnnnnnnn name null");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "product's name not null ", "")
            );
        }

        Optional<Category> category = categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            System.out.println("134 nnnnnnnnnnnnnnnnnnn category not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "not found category's id: " + categoryId, "")
            );
        }
        System.out.println("139 nnnnnnnnnnnnnnnnnnn qua het if");
        Category category1 = category.get();
        Product product = new Product(
                name.trim(),
                description.trim(),
                published,
                category1
        );


        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("failed", "add new product success ", productRepository.save(product))
        );
    }

    /**
     * many to one
     * add new product
     * get product by category id
     * WITH IMAGE -- PathVariable category id --> RequestParam
     * from raw --> into form-data and PathVariable category id --> RequestParam
     * http://localhost:8082/api/v1/productImage/insert/product
     */
    @PostMapping("/productImage/insert/product")
    public ResponseEntity<ResponseObject> insertProductWithImage(
            @RequestParam Long categoryId,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam boolean published
    ) {

        if (productRepository.findByName(name.trim()).size() > 0) {
            System.out.println("120 nnnnnnnnnnnnnnnnnnn name exits");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "exits product's name: " + name, "")
            );
        }
        if (name.trim().equals("")) {
            System.out.println("126 nnnnnnnnnnnnnnnnnnn name null");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "product's name not null ", "")
            );
        }

        Optional<Category> category = categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            System.out.println("134 nnnnnnnnnnnnnnnnnnn category not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "not found category's id: " + categoryId, "")
            );
        }
        System.out.println("139 nnnnnnnnnnnnnnnnnnn qua het if");
        Category category1 = category.get();
        Product product = new Product(
                name.trim(),
                description.trim(),
                published,
                category1
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("failed", "add new product success ", productRepository.save(product))
        );
    }

    /**
     * many to one
     * add new product
     * get product by category id
     * WITH IMAGE -- insert request param AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
     * http://localhost:8082/api/v1/productImage/insert/product/a
     */
    @PostMapping("/productImage/insert/product/a")
    public ResponseEntity<ResponseObject> insertProductWithImageRequest(
            @RequestParam Long categoryId,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam boolean published,
            @RequestParam("file") MultipartFile file
            ) {

        if (productRepository.findByName(name.trim()).size() > 0) {
            System.out.println("120 nnnnnnnnnnnnnnnnnnn name exits");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "exits product's name: " + name, "")
            );
        }
        if (name.trim().equals("")) {
            System.out.println("126 nnnnnnnnnnnnnnnnnnn name null");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "product's name not null ", "")
            );
        }

        Optional<Category> category = categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            System.out.println("134 nnnnnnnnnnnnnnnnnnn category not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "not found category's id: " + categoryId, "")
            );
        }
        if (file == null) {
            System.out.println("251 nnnnnnnnnnnnnnnnnnn file image not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "not found file's image: ", "")
            );
        } else {
            System.out.println("256 nnnnnnnnnnnnnnnnnnn file exits");
            System.out.println("qaz: " + file.getName());
            System.out.println("zxc: " + file.getOriginalFilename());
        }
        System.out.println("139 nnnnnnnnnnnnnnnnnnn qua het if");

//        String generatedFile = iStorageService.uploadFile(file);
//        System.out.println("224 44444444444444444444444444444444444444444: " + generatedFile);
//        if (generatedFile != null) {
//            System.out.println("224 44444444444444444444444444444444444444444: " + generatedFile);
//        } else {
//            System.out.println("224 44444444444444444444444444444444444444444: nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn" );
//        }

        try {
            System.out.println("275 iiiiiiiiiiiiiii start try catch image -- new product");
            Category categoryOfProduct = category.get();
            Product product = new Product(
                    name.trim(),
                    description.trim(),
                    published,
                    categoryOfProduct
            );
//            productRepository.save(product);
//
//            iStorageService.uploadImageProduct(Long.parseLong("34"), file);

            System.out.println("283 iiiiiiiiiiiiiii end try catch image -- new product");
        } catch (Exception ex) {
            ex.getStackTrace();
        }
/**
        try {
            System.out.println("253 iiiiiiiiiiiiiii start try catch image -- new product");
            Category category1 = category.get();
            Product product = new Product(
                    name.trim(),
                    description.trim(),
                    published,
                    category1
            );
            System.out.println("261 iiiiiiiiiiiiiii end try catch image -- new product");

            String generatedFile = iStorageService.uploadFile(file);
            System.out.println("224 44444444444444444444444444444444444444444: " + generatedFile);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(baseURL)
                    .path("/uploadPhoto/")
                    .path(generatedFile)
                    .toUriString();
            System.out.println("230 4444444444444       URL     4444444444444444444: " + fileDownloadUri);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "upload image file successfully", generatedFile)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed","" + ex.getMessage(), "")
            );
        }

*/
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("failed", "add new product success ", "productRepository.save(product)")
        );
    }
}
