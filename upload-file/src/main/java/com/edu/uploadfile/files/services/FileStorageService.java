package com.edu.uploadfile.files.services;

import com.edu.uploadfile.files.models.FileDB;
import com.edu.uploadfile.files.repositories.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    @Autowired
    private FileDBRepository fileDBRepository;

    public FileDB store(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes());

        return fileDBRepository.save(fileDB);
    }

    public Stream<FileDB> mulStore(MultipartFile[] files) throws Exception {
        List<FileDB> fileDBS = new ArrayList<>();
          Arrays.asList(files).stream().forEach(
                  (item) -> {
                      String fileName = StringUtils.cleanPath(item.getOriginalFilename());
                      FileDB fileDB = null;
                      try {
                          fileDB = new FileDB(fileName, item.getContentType(), item.getBytes());
                          fileDBS.add(fileDB);
                      } catch (IOException e) {
                          System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx");
                          throw new RuntimeException(e);
                      }
//                      fileDBRepository.save(fileDB);
                  }
          );
          return fileDBRepository.saveAll(fileDBS).stream();
//          return fileDBRepository.findAll().stream();
    }

    public FileDB getFile(Long id) {
        return fileDBRepository.findById(id).get();
    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }
}
