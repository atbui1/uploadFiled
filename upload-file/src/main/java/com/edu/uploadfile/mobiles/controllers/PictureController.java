package com.edu.uploadfile.mobiles.controllers;

import com.edu.uploadfile.mobiles.models.Image;
import com.edu.uploadfile.mobiles.models.Picture;
import com.edu.uploadfile.mobiles.payloads.response.ImageResponse;
import com.edu.uploadfile.mobiles.payloads.response.ResponseObject;
import com.edu.uploadfile.mobiles.repositories.ImageRepository;
import com.edu.uploadfile.mobiles.repositories.PictureRepository;
import com.edu.uploadfile.mobiles.services.iservice.IPictureService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://localhost:8082")
@RestController
@RequestMapping(path = "/api/v1")
public class PictureController {

public static final String baseURL = "/api/v1";

    @Autowired
    IPictureService iPictureService;
    @Autowired
    PictureRepository pictureRepository;

    /**
     * http://localhost:8082/api/v1/p/uploadPhoto
     * response link local host http://localhost:8082/api/v1/uploadPhoto/4c4bcfda38934b4dbe1aaa28c7842171.jpg
     * */
    @PostMapping("/p/uploadPhoto")
    public ResponseEntity<ResponseObject> uploadPhoto(@RequestParam("file")MultipartFile file) {
        try {
            String generatedFile = iPictureService.storeFile(file);
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
    /** upload multi images -- both db and folder
     * http://localhost:8082/api/v1/p/uploadPhotoMulti
     * upload many files
     * */
    @PostMapping("/p/uploadPhotoMulti")
    public ResponseEntity<ResponseObject> uploadPhotoMulti(@RequestParam("file")MultipartFile[] files) {
        try {
            List<String> generatedFiles = new ArrayList<>();
            generatedFiles = iPictureService.storeMultiFile(files);
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
     * load the file
     */
    @GetMapping("/p/uploadFile/{fileName:.+}")
    public ResponseEntity<?> loadFile(@PathVariable(value = "fileName") String fileName) {
        try {
            Resource file = iPictureService.loadFiled(fileName);
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
     * load the file
     */
    @GetMapping("/p/uploadPhoto/{fileName:.+}")
    public ResponseEntity<?> getDetailImage(@PathVariable(value = "fileName") String fileName) {
        try {
            byte[] bytes = iPictureService.readFileContent(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "read detail file failed", "")
            );
        }
    }

    @GetMapping("/p/uploadPhoto")
    public ResponseEntity<?> getAllImage() {
        try {
            List<String> files = iPictureService.loadAll()
                    .map(path -> {
                        String urlPath = MvcUriComponentsBuilder.fromMethodName(
                                //convert filename to url(send request "getDetailImage")
                                PictureController.class,
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
                    new ResponseObject("ok", "get all images successfully", files)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "not found list images", "")
            );
        }
    }
    /**
     * upload file
     * success
     * http://localhost:8082/api/v1/p/uploadPhotoNew
     * response object picture
     */
    @PostMapping("/p/uploadPhotoNew")
    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file")MultipartFile file) {
        try {
            System.out.println("157 PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPp 157");
            String generatedFile = iPictureService.uploadFile(file);
            System.out.println("156 44444444444444444444444444444444444444444: " + generatedFile);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(baseURL)
                    .path("/uploadPhoto/")
                    .path(generatedFile)
                    .toUriString();
            System.out.println("162 4444444444444       URL     4444444444444444444: " + fileDownloadUri);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "upload image file successfully", pictureRepository.findByName(generatedFile))
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
    @GetMapping("/p/uploadPhoto/download/{fileId}")
    public ResponseEntity<?> downloadFileById(@PathVariable(value = "fileId") Long fileId) {
        try {
            Picture picture = iPictureService.downloadById(fileId);
            byte[] bytes = iPictureService.readFileContent(picture.getName());
            System.out.println("1411111111111111111111: "+ picture);
            System.out.println("2222222222222222222222: "+ bytes);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(picture.getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + picture.getName() + "\"")
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
    @GetMapping("/p/uploadPhoto/downloads/{filename}")
    public ResponseEntity<?> downloadFileByName(@PathVariable(value = "filename") String filename) {
        try {
            Picture picture = iPictureService.downloadByName(filename);
            byte[] bytes = iPictureService.readFileContent(picture.getName());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(picture.getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + picture.getName() + "\"")
                    .body(new ByteArrayResource(bytes));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "download file by name failed", "")
            );
        }
    }


    /** 111111111111111111111111111111111111111111111111111111111111111111111
     * uploadFile -- nStore
     * upload single file
     * response object picture
     */
    @PostMapping("/p/n/uploadPhoto")
    public ResponseEntity<ResponseObject> nStore(@RequestParam("file")MultipartFile file) {
        try {
//            Image image = iPictureService.nStore(file);
            Picture picture = iPictureService.nStore(file);
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(baseURL)
                    .path("/n/readFile/")
                    .path(picture.getName())
                    .toUriString();
            System.out.println("4444444444444       URL     4444444444444444444: " + imageUrl);
            picture.setUrl(imageUrl);
            pictureRepository.save(picture);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "nStore successfully", picture)
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
    @GetMapping("/p/n/readFile/{fileName:.+}")
    public ResponseEntity<?> nReadFile(@PathVariable(value = "fileName") String fileName) {
        try {
            byte[] bytes = iPictureService.nReadFileContent(fileName);
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
    @GetMapping("/p/n/readFiles/{fileName:.+}")
    public ResponseEntity<?> nReadFiles(@PathVariable(value = "fileName") String fileName) {
        try {
            Picture picture = pictureRepository.findByName(fileName);
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(baseURL)
                    .path("/n/readFile/")
                    .path(picture.getName())
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
    @GetMapping("/p/n/uploadPhoto")
    public ResponseEntity<?> nLoadAllFile() {
        try {
            List<String> files = iPictureService.nLoadAllFile()
                    .map(path -> {
                        String urlPath = MvcUriComponentsBuilder.fromMethodName(
                                        //convert filename to url(send request "getDetailImage")
                                        PictureController.class,
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
    @GetMapping("/p/n/uploadPhotos")
    public ResponseEntity<?> nLoadAllFiles() {
        try {
            List<Picture> pictures = pictureRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "get all images db successfully", pictures)
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
    @GetMapping("/p/n/uploadPhotos1")
    public ResponseEntity<?> nLoadAllFileImage() {
        try {
            List<ImageResponse> images = iPictureService.nLoadAllFileImage().map(dbFile -> {
                String fileDownloadUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path(baseURL)
                        .path("/p/n/readFile/")
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
