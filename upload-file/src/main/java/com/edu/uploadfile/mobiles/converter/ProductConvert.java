package com.edu.uploadfile.mobiles.converter;

import com.edu.uploadfile.mobiles.models.Image;
import com.edu.uploadfile.mobiles.models.Product;
import com.edu.uploadfile.mobiles.payloads.requests.ProductRequest;
import org.springframework.stereotype.Component;

@Component
public class ProductConvert {

    public Product toEntity(ProductRequest dto) {
        Product entity = new Product();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPublished(dto.isPublished());
        entity.setCategory(dto.getCategory());
        entity.setImages(dto.getImages());

        return entity;
    }

    public Product toEntity(Product entity, ProductRequest dto) {

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPublished(dto.isPublished());
        entity.setCategory(dto.getCategory());
        System.out.println(" 28 111111*************************************************");
        entity.deleteImage();
        System.out.println("30 222222*************************************************");
        entity.setImages(dto.getImages());

        return entity;
    }
    public ProductRequest toDTO(Product entity) {
        ProductRequest dto = new ProductRequest();
        if (entity.getId() != null) {
            dto.setId(entity.getId());
        }
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCategory(entity.getCategory());
        dto.setImages(entity.getImages());

        return dto;
    }
}
