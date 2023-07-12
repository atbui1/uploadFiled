package com.edu.uploadfile.mobiles.controllers;

import com.edu.uploadfile.mobiles.models.Image;
import com.edu.uploadfile.mobiles.payloads.response.ImageResponse;
import com.edu.uploadfile.mobiles.payloads.response.ResponseObject;
import com.edu.uploadfile.mobiles.repositories.ImageRepository;
import com.edu.uploadfile.mobiles.services.iservice.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1")
public class UploadImageController {

public static final String baseURL = "/api/v1";
    @Autowired
    IStorageService iStorageService;
    @Autowired
    ImageRepository imageRepository;

    @PostMapping("/uploadPhoto")
    public ResponseEntity<ResponseObject> uploadPhoto(@RequestParam("file")MultipartFile file) {
        try {
            String generatedFile = iStorageService.storeFile(file);
            System.out.println("44444444444444444   uploadPhoto     444444444444444444444444: " + generatedFile);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(baseURL)
                    .path("/uploadPhoto/")
                    .path(generatedFile)
                    .toUriString();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "upload image file successfully", fileDownloadUri)
//                    new ResponseObject("ok", "upload image file successfully", generatedFile)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed","" + ex.getMessage(), "")
            );
        }
    }
    /** upload multi images
     * http://localhost:8082/api/v1/uploadPhotoMulti
     * */
    @PostMapping("/uploadPhotoMulti")
    public ResponseEntity<ResponseObject> uploadPhotoMulti(@RequestParam("file")MultipartFile[] files) {
        try {
            List<String> generatedFiles = new ArrayList<>();
            generatedFiles = iStorageService.storeMultiFile(files);
            System.out.println("44444444444444444444444444444444444444444 day nek: " + generatedFiles);
//            if (generatedFiles.size() > 0) {
//
//                return ResponseEntity.status(HttpStatus.OK).body(
//                        new ResponseObject("ok", "upload image file successfully", generatedFiles)
//                );
//            }
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    new ResponseObject("ok", "upload image file failed...", "")
//            );
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "upload image file successfully", generatedFiles)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed","" + ex.getMessage(), "")
            );
        }
    }
    /**
     * --> one
     * load the file --> source code
     */
    @GetMapping("/uploadFile/{fileName:.+}")
    public ResponseEntity<?> loadFile(@PathVariable(value = "fileName") String fileName) {
        try {
            Resource file = iStorageService.loadFiled(fileName);
//            return ResponseEntity.status(HttpStatus.OK).body(
//                    new ResponseObject("ok", "load file successfully", "")
//            );
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "load file failed", "")
            );
        }
    }
    /**
     * --> two
     * load the file --> show image
     */
    @GetMapping("/uploadPhoto/{fileName:.+}")
    public ResponseEntity<?> getDetailImage(@PathVariable(value = "fileName") String fileName) {
        try {
            byte[] bytes = iStorageService.readFileContent(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "read detail file failed", "")
            );
        }
    }

    /** load images from storage folder uploads */
    @GetMapping("/uploadPhoto")
    public ResponseEntity<?> getAllImage() {
        try {
            List<String> files = iStorageService.loadAll()
                    .map(path -> {
                        String urlPath = MvcUriComponentsBuilder.fromMethodName(
                                //convert filename to url(send request "getDetailImage")
                                UploadImageController.class,
                                "getDetailImage",
                                path.getFileName().toString())
                                .build()
                                .toUri()
                                .toString();
                        return urlPath;
                    }).collect(Collectors.toList());
            System.out.println("5555555555555555555555555555555555555555555555555555555555555555555");
            System.out.println("66666666666: " + files);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "get all images successfully <--> load-all images from storage folder", files)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "not found list images", "")
            );
        }
    }

    /**
     * upload file into both db and folder
     * success
     * call imageRepository in imageStoreServiceImp
     */
    @PostMapping("/uploadPhotoNew")
    public ResponseEntity<ResponseObject> uploadFileToBoth(@RequestParam("file")MultipartFile file) {
        try {
            System.out.println("154 AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa 154 uploadPhotoNew");
            String generatedFile = iStorageService.uploadFile(file);
            System.out.println("44444444444444444444444444444444444444444: " + generatedFile);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(baseURL)
                    .path("/uploadPhoto/")
                    .path(generatedFile)
                    .toUriString();
            System.out.println("4444444444444       URL     4444444444444444444: " + fileDownloadUri);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "upload image file successfully", generatedFile)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed","" + ex.getMessage(), "")
            );
        }
    }

    /**
     * download file by id
     * success
     */
    @GetMapping("/uploadPhoto/download/{fileId}")
    public ResponseEntity<?> downloadFileById(@PathVariable(value = "fileId") Long fileId) {
        try {
            Image image = iStorageService.downloadById(fileId);
            byte[] bytes = iStorageService.readFileContent(image.getName());
            System.out.println("1411111111111111111111: "+ image);
            System.out.println("2222222222222222222222: "+ bytes);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(image.getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                    .body(new ByteArrayResource(bytes));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "download file by id failed", "")
            );
        }
    }

    /**
     * download file by name
     * success
     */
    @GetMapping("/uploadPhoto/downloads/{filename}")
    public ResponseEntity<?> downloadFileByName(@PathVariable(value = "filename") String filename) {
        try {
            Image image = iStorageService.downloadByName(filename);
            byte[] bytes = iStorageService.readFileContent(image.getName());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(image.getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                    .body(new ByteArrayResource(bytes));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "download file by name failed", "")
            );
        }
    }


    /** 111111111111111111111111111111111111111111111111111111111111111111111
     * uploadFile -- nStore
     */
    @PostMapping("/n/uploadPhoto")
    public ResponseEntity<ResponseObject> nStore(@RequestParam("file")MultipartFile file) {
        try {
            Image image = iStorageService.nStore(file);
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(baseURL)
                    .path("/n/readFile/")
                    .path(image.getName())
                    .toUriString();
            System.out.println("4444444444444       URL     4444444444444444444: " + imageUrl);
            image.setUrl(imageUrl);
            imageRepository.save(image);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "nStore successfully", image)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed","nStore failed..." + ex.getMessage(), "")
            );
        }
    }

    /**
     * show img in url 1
     */
    @GetMapping("/n/readFile/{fileName:.+}")
    public ResponseEntity<?> nReadFile(@PathVariable(value = "fileName") String fileName) {
        try {
            byte[] bytes = iStorageService.nReadFileContent(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "read detail file failed", "")
            );
        }
    }

    /**
     * show img in url 2
     */
    @GetMapping("/n/readFiles/{fileName:.+}")
    public ResponseEntity<?> nReadFiles(@PathVariable(value = "fileName") String fileName) {
        try {
            Image image = imageRepository.findByName(fileName);
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(baseURL)
                    .path("/n/readFile/")
                    .path(image.getName())
                    .toUriString();
            System.out.println("4444444444444       URL     4444444444444444444: " + imageUrl);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("qaz", "read detail file failed", imageUrl)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "read detail file failed", "")
            );
        }
    }

    /**
     * load all file to url
     */
    @GetMapping("/n/uploadPhoto")
    public ResponseEntity<?> nLoadAllFile() {
        try {
            List<String> files = iStorageService.nLoadAllFile()
                    .map(path -> {
                        String urlPath = MvcUriComponentsBuilder.fromMethodName(
                                        //convert filename to url(send request "getDetailImage")
                                        UploadImageController.class,
                                        "nReadFile",
                                        path.getFileName().toString())
                                .build()
                                .toUri()
                                .toString();
                        return urlPath;
                    }).collect(Collectors.toList());

            System.out.println("5555555555555555555555555555555555555555555555555555555555555555555");
            System.out.println("66666666666: " + files);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "get all images link successfully", files)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "not found list images", "")
            );
        }
    }

    /**
     * load all file to url
     */
    @GetMapping("/n/uploadPhotos")
    public ResponseEntity<?> nLoadAllFiles() {
        try {
            List<Image> images = imageRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "get all images db successfully", images)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "not found list images", "")
            );
        }
    }
    /**
     * load all file to url xxx
     */
    @GetMapping("/n/uploadPhotos1")
    public ResponseEntity<?> nLoadAllFileImage() {
        try {
            List<ImageResponse> images = iStorageService.nLoadAllFileImage().map(dbFile -> {
                String fileDownloadUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path(baseURL)
                        .path("/n/readFile/")
                        .path(dbFile.getName())
                        .toUriString();

                return new ImageResponse(
                        dbFile.getId(),
                        dbFile.getName(),
                        dbFile.getType(),
                        dbFile.getUriLocal(),
                        fileDownloadUri);
            }).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "get all images s1 db successfully", images)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "not found list s1 images", "")
            );
        }
    }


}
