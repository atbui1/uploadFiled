package com.edu.uploadfile.mobiles.services.iservice;

import com.edu.uploadfile.mobiles.models.Product;
import com.edu.uploadfile.mobiles.payloads.requests.ProductRequest;

import java.util.List;

public interface IProductService {

    List<Product> getAllProduct();
    List<ProductRequest> getAllProductRequest();
    Product getProductById(Long id);

    Product upsertProductNoImageNoForm(ProductRequest productRequest);
    Product upsertProductNoImageFormData(ProductRequest productRequest);
    Product upsertProductWithImageNoForm(ProductRequest productRequest);
    Product upsertProductWithImagesNoForm(ProductRequest productRequest);
    Product upsertProductWithImages(ProductRequest productRequest);

}
