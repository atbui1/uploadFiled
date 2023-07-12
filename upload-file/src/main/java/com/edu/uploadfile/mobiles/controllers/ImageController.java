package com.edu.uploadfile.mobiles.controllers;


import com.edu.uploadfile.mobiles.payloads.requests.ImageRequest;
import com.edu.uploadfile.mobiles.payloads.response.ImageResponse;
import com.edu.uploadfile.mobiles.payloads.response.ResponseObject;
import com.edu.uploadfile.mobiles.services.serviceImpl.ImageServiceIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8082")
@RestController
@RequestMapping(path = "/api/v2")
public class ImageController {
    public static final String baseURL = "/api/v2";
    @Autowired
    ImageServiceIml imageServiceIml;


    @GetMapping("/image")
    ResponseEntity<?> getImages() {
        System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        List<ImageRequest> imageRequests = imageServiceIml.nLoadAllFileImage().collect(Collectors.toList());;
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "get all images s1 db successfully convert to url", imageRequests)
        );
    }

    /**
     * --> two
     * load the file image on url
     */
    @GetMapping("/image/show-image-url/{fileName:.+}")
    public ResponseEntity<?> getDetailImage(@PathVariable(value = "fileName") String fileName) {
        try {
            byte[] bytes = imageServiceIml.readFileContent(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "" + ex, "")
            );
        }
    }

    @DeleteMapping("/image/delete/{id}")
    ResponseEntity<?> deleteById(@PathVariable(value = "id") Long id) {
        imageServiceIml.deleteFileFolder(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "ok2", "ok3")
        );
    }
}
