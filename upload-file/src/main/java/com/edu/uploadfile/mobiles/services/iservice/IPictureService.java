package com.edu.uploadfile.mobiles.services.iservice;

import com.edu.uploadfile.mobiles.models.Image;
import com.edu.uploadfile.mobiles.models.Picture;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface IPictureService {
    public String storeFile(MultipartFile file); //------
    /** insert image bot db and folder */
    public String uploadFile(MultipartFile file);
    /** insert IMAGES bot db and folder */
    public List<String> storeMultiFile(MultipartFile[] files);
    public Resource loadFiled(String filename);
    /** load images from storage folder */
    public Stream<Path> loadAll();
    public byte[] readFileContent(String filename); //----
    public Picture downloadById(Long fileId);
    public Picture downloadByName(String filename);
    public void deleteAllFile();

    /** upload one images */
    public Picture nStore(MultipartFile file);
    public byte[] nReadFileContent(String filename);
    public Stream<Path> nLoadAllFile();
    public Stream<Picture> nLoadAllFileImage();
}
