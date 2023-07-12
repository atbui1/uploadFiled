package com.edu.uploadfile.mobiles.payloads.requests;

import com.edu.uploadfile.mobiles.models.Category;
import com.edu.uploadfile.mobiles.models.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequest {
    private Long id;
    private String name;
    private String description;
    private boolean published;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;
    private Set<Image> images;

    private Long category_id;
    private MultipartFile file;
    private MultipartFile[] files;

    @Override
    public String toString() {
        return "ProductRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", published=" + published +
                ", category_id=" + category_id +
                ", category=" + category +
                ", images=" + images +
                '}';
    }
}
