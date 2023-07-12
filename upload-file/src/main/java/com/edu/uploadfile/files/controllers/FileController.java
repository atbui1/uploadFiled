package com.edu.uploadfile.files.controllers;

import com.edu.uploadfile.files.models.FileDB;
import com.edu.uploadfile.files.payload.responses.ResponseFile;
import com.edu.uploadfile.files.payload.responses.ResponseObject;
import com.edu.uploadfile.files.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            fileStorageService.store(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Uploaded the file successfully: " + file.getOriginalFilename(), "")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Could not upload the file: " + file.getOriginalFilename(), file)
            );
        }
    }
    @PostMapping("/upload/mul")
    ResponseEntity<?> uploadMul(@RequestParam("file") MultipartFile[] files) {
        String msg = "";
        try {
            List<String> fileNames = new ArrayList<>();
//            fileStorageService.mulStore(files);
//            fileStorageService.mulStore(files).forEach((a)->{fileNames.add(a.getName());});

            List<ResponseFile> result = fileStorageService.mulStore(files).map(
                    (item) -> {
                        String fileUri = ServletUriComponentsBuilder
                                .fromCurrentContextPath()
                                .path("/api/v1/file/")
                                .path(item.getId().toString())
                                .toUriString();

                        return new ResponseFile(item.getId(), item.getName(), fileUri, item.getType(), item.getData().length);
//                        return new ResponseFile(item.getName(), fileUri, item.getType(), item.getData().length);
                    }
            ).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Uploaded the file mul successfully qaz: " + fileNames, result)
//                    new ResponseObject("ok", "Uploaded the file mul successfully qaz: " + fileNames, fileStorageService.mulStore(files))
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "Could not upload the mul file: ", files)
            );
        }
    }

    @GetMapping("/file/{fileId}")
    ResponseEntity<?> getFile(@PathVariable(value = "fileId") Long fileId) {
        try {
            FileDB fileDB = fileStorageService.getFile(fileId);
            if (fileDB != null) {
//                return ResponseEntity.status(HttpStatus.OK).body(
//                        new ResponseObject("ok", "Found file's id: " + fileId, fileDB.getData())
//                );

//                return ResponseEntity.ok()
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
//                        .body(fileDB.getData());
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(fileDB.getType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                        .body(new ByteArrayResource(fileDB.getData()));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Not found file's id: " + fileId, "")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Server err" , "")
            );
        }
    }

    @GetMapping("/file")
    ResponseEntity<?> getAllFile() {
        List<ResponseFile> files = fileStorageService.getAllFiles().map(
                (item) -> {
                    String fileUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/api/v1/file/")
                            .path(item.getId().toString())
                            .toUriString();

                    return new ResponseFile(item.getId(), item.getName(), fileUri, item.getType(), item.getData().length);
//                    return new ResponseFile(item.getName(), fileUri, item.getType(), item.getData().length);
                }
        ).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Found files: ", files)
//                files
        );
    }
}
