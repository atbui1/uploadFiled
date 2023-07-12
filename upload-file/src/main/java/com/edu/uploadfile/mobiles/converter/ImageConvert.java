package com.edu.uploadfile.mobiles.converter;

import com.edu.uploadfile.mobiles.models.Image;
import com.edu.uploadfile.mobiles.payloads.requests.ImageRequest;
import org.springframework.stereotype.Component;

@Component
public class ImageConvert {

    public Image toEntity(ImageRequest dto) {
        Image entity = new Image();

        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setUrl(dto.getUrl());
        entity.setUriLocal(dto.getUriLocal());

        return entity;
    }

    public ImageRequest toDTo(Image entity) {
        ImageRequest dto = new ImageRequest();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setUrl(entity.getUrl());
        dto.setUriLocal(entity.getUriLocal());

        return dto;
    }
}
