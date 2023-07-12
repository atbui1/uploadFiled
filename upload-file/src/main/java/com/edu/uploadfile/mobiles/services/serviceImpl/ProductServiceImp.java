package com.edu.uploadfile.mobiles.services.serviceImpl;

import com.edu.uploadfile.mobiles.converter.ImageConvert;
import com.edu.uploadfile.mobiles.converter.ProductConvert;
import com.edu.uploadfile.mobiles.exception.ApiException;
import com.edu.uploadfile.mobiles.exception.NotFoundException;
import com.edu.uploadfile.mobiles.models.Category;
import com.edu.uploadfile.mobiles.models.Image;
import com.edu.uploadfile.mobiles.models.Product;
import com.edu.uploadfile.mobiles.models.User;
import com.edu.uploadfile.mobiles.payloads.requests.ImageRequest;
import com.edu.uploadfile.mobiles.payloads.requests.ProductRequest;
import com.edu.uploadfile.mobiles.repositories.CategoryRepository;
import com.edu.uploadfile.mobiles.repositories.ImageRepository;
import com.edu.uploadfile.mobiles.repositories.ProductRepository;
import com.edu.uploadfile.mobiles.services.iservice.IImageService;
import com.edu.uploadfile.mobiles.services.iservice.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.awt.image.ImageProducer;
import java.util.*;

@Service
public class ProductServiceImp implements IProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    ImageServiceIml imageServiceIml;

    @Autowired
    ProductConvert productConvert;

    @Autowired
    ImageConvert imageConvert;

    @Override
    public List<Product> getAllProduct() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    @Override
    public List<ProductRequest> getAllProductRequest() {
        List<Product> products = productRepository.findAll();
        List<ProductRequest> productRequests = new ArrayList<>();
        ProductRequest productRequest = new ProductRequest();
        System.out.println("66666666666666666666666666666666666 size: " + products.size());
        for (Product x: products) {
            System.out.println("tmp i");
            productRequest = productConvert.toDTO(x);
            System.out.println("tmp i value: " + productRequest);
            productRequests.add(productRequest);
        }
        System.out.println("xxx: " + productRequests);
        return productRequests;
    }

    @Override
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) {
            throw  new NotFoundException("not found product's id: " + id);
        }
        return product.get();
    }

    @Override
    public Product upsertProductNoImageNoForm(ProductRequest productRequest) {
        Category category = new Category();
        if (productRequest.getCategory_id() == null) {
            throw new NotFoundException("not found category_id");
        }
        if (!categoryRepository.existsById(productRequest.getCategory_id())) {
            throw new NotFoundException("not found category by id");
        } else {
            category = categoryRepository.findById(productRequest.getCategory_id()).get();
        }
        productRequest.setCategory(category);
        Product product = new Product();

        if (productRequest.getId() != null) {
            if (productRepository.existsById(productRequest.getId())) {
                Product oldEntity = productRepository.findById(productRequest.getId()).get();
                product = productConvert.toEntity(oldEntity, productRequest);
            } else {
                throw new NotFoundException("not found product by id: " + productRequest.getId());
            }
        } else {
            product = productConvert.toEntity(productRequest);
        }
        productRepository.save(product);
        return productRepository.findByName(productRequest.getName()).get(0);
    }

    @Override
    public Product upsertProductNoImageFormData(ProductRequest productRequest) {
        Category category = new Category();
        if (productRequest.getCategory_id() == null) {
            throw new NotFoundException("not found category_id");
        }
        if (productRepository.existsByName(productRequest.getName().trim())) {
            throw new NotFoundException("exists name");
        }
        if (!categoryRepository.existsById(productRequest.getCategory_id())) {
            throw new NotFoundException("not found category by id");
        } else {
            category = categoryRepository.findById(productRequest.getCategory_id()).get();
        }
        productRequest.setCategory(category);
        Product product = new Product();

        if (productRequest.getId() != null) {
            if (productRepository.existsById(productRequest.getId())) {
                Product oldEntity = productRepository.findById(productRequest.getId()).get();
                product = productConvert.toEntity(oldEntity, productRequest);
            } else {
                throw new NotFoundException("not found product by id: " + productRequest.getId());
            }
        } else {
            product = productConvert.toEntity(productRequest);
        }
        productRepository.save(product);
        return productRepository.findByName(productRequest.getName()).get(0);
    }

    @Override
    public Product upsertProductWithImageNoForm(ProductRequest productRequest) {
        Category category = new Category();
        Product product = new Product();

        if (productRequest.getFile() == null) {
            throw new NotFoundException("not found file image");
        }
        if (productRepository.existsByName(productRequest.getName().trim())) {
            throw new NotFoundException("exists name");
        }

        if (productRequest.getCategory_id() == null) {
            throw new NotFoundException("not found category_id");
        }
        if (!categoryRepository.existsById(productRequest.getCategory_id())) {
            throw new NotFoundException("not found category by id");
        } else {
            category = categoryRepository.findById(productRequest.getCategory_id()).get();
        }

        productRequest.setCategory(category);

        if (productRequest.getId() != null) {
            if (productRepository.existsById(productRequest.getId())) {
                Product oldEntity = productRepository.findById(productRequest.getId()).get();
                product = productConvert.toEntity(oldEntity, productRequest);
            } else {
                throw new NotFoundException("not found product by id: " + productRequest.getId());
            }
        } else {
            product = productConvert.toEntity(productRequest);
        }
        productRepository.save(product);

        Long productId = productRepository.findByName(productRequest.getName()).get(0).getId();

        imageServiceIml.uploadOneImage(productRequest.getFile(), productId);

        return productRepository.findById(productId).get();
//        return productRepository.findByName(productRequest.getName()).get(0);
    }

    @Override
    public Product upsertProductWithImagesNoForm(ProductRequest productRequest) {
        System.out.println("178 productIMP ------------------------------------");
        Category category = new Category();
        Product product = new Product();

        if (productRequest.getFiles() == null) {
            throw new NotFoundException("not found file image");
        }
        try {
            MultipartFile[] multipartFiles = productRequest.getFiles();
            for (MultipartFile x: multipartFiles) {
                imageServiceIml.validImageTest(x);
            }
            System.out.println("tat ca la image 190 productIMP");
        } catch (Exception e) {
            throw new NotFoundException("Kiem tr lai file anh");
        }
//        if (productRepository.existsByName(productRequest.getName().trim())) {
//            throw new NotFoundException("exists name");
//        }

        if (productRequest.getCategory_id() == null) {
            throw new NotFoundException("not found category_id");
        }
        if (!categoryRepository.existsById(productRequest.getCategory_id())) {
            throw new NotFoundException("not found category by id");
        } else {
            category = categoryRepository.findById(productRequest.getCategory_id()).get();
        }

        System.out.println("207 productIMP -------------------------------------------");
        productRequest.setCategory(category);

        if (productRequest.getId() != null) {
            if (productRepository.existsById(productRequest.getId())) {
                Product oldEntity = productRepository.findById(productRequest.getId()).get();
                product = productConvert.toEntity(oldEntity, productRequest);
            } else {
                throw new NotFoundException("not found product by id: " + productRequest.getId());
            }
        } else {
            if (productRepository.existsByName(productRequest.getName().trim())) {
                throw new NotFoundException("exists name");
            }
            product = productConvert.toEntity(productRequest);
        }
        System.out.println("223 productIMP--------------------------------------------------");
        productRepository.save(product);

        Long productId = productRepository.findByName(productRequest.getName()).get(0).getId();
        System.out.println("product id: " + productId);

        imageServiceIml.uploadManyImages(productRequest.getFiles(), productId);
        System.out.println("229 productIMP-----------------------------------------------------");

        return productRepository.findById(productId).get();
//        return productRepository.findByName(productRequest.getName()).get(0);
    }

    @Override
    public Product upsertProductWithImages(ProductRequest productRequest) {
        System.out.println("239 productIMP ------------------------------------");
        Category category = new Category();
        Product product = new Product();
        Set<Image> images = new HashSet<>();

        if (productRequest.getFiles() == null) {
            throw new NotFoundException("not found file image");
        }
        try {
            MultipartFile[] multipartFiles = productRequest.getFiles();
            images = imageServiceIml.validImageTestQAZ(multipartFiles);
            System.out.println("tat ca la image 249 productIMP");
        } catch (Exception e) {
            throw new NotFoundException("Kiem tr lai file anh");
        }
//        if (productRepository.existsByName(productRequest.getName().trim())) {
//            throw new NotFoundException("exists name");
//        }

        if (productRequest.getCategory_id() == null) {
            throw new NotFoundException("not found category_id");
        }
        if (!categoryRepository.existsById(productRequest.getCategory_id())) {
            throw new NotFoundException("not found category by id");
        } else {
            category = categoryRepository.findById(productRequest.getCategory_id()).get();
        }

        System.out.println("207 productIMP -------------------------------------------");
        productRequest.setCategory(category);
//        productRequest.setImages(images);

        if (productRequest.getId() != null) {
            if (productRepository.existsById(productRequest.getId())) {
                System.out.println("co product's id --> update product");
                Product oldEntity = productRepository.findById(productRequest.getId()).get();
                product = productConvert.toEntity(oldEntity, productRequest);
            } else {
                throw new NotFoundException("not found product by id: " + productRequest.getId());
            }
        } else {
            if (productRepository.existsByName(productRequest.getName().trim())) {
                throw new NotFoundException("exists name");
            }
            System.out.println("kg co product's id --> add new product");
            product = productConvert.toEntity(productRequest);
        }
        System.out.println("223 productIMP--------------------------------------------------");
        System.out.println("223 images[]: " + images);
        System.out.println("223 product: " + product);
        productRepository.save(product);

        System.out.println("555555555555555555555555555555555555555555555555");

        Long productId = productRepository.findByName(productRequest.getName()).get(0).getId();
        System.out.println("product id: " + productId);
        for (Image x: images) {
            x.setProduct(product);
            imageRepository.save(x);
        }
        product.setImages(images);

//        imageServiceIml.uploadManyImages(productRequest.getFiles(), productId);
        System.out.println("229 productIMP-----------------------------------------------------");

        return productRepository.findById(productId).get();
    }


}
