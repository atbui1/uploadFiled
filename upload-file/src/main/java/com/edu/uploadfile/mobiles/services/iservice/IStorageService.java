package com.edu.uploadfile.mobiles.services.iservice;

import com.edu.uploadfile.mobiles.models.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface IStorageService {
    public String storeFile(MultipartFile file); //------
    public Resource loadFiled(String filename);

    /**
     * load images from storage folder
     */
    public Stream<Path> loadAll();
    public byte[] readFileContent(String filename); //----
    public Image downloadById(Long fileId);
    public Image downloadByName(String filename);
    public void deleteAllFile();

    public Image nStore(MultipartFile file);
    public byte[] nReadFileContent(String filename);
    public Stream<Path> nLoadAllFile();
    public Stream<Image> nLoadAllFileImage();

    public String uploadImageProduct(Long product_id, MultipartFile file);

//    222222222222222222222222222222222222
    /** insert image bot db and folder */
    public String uploadFile(MultipartFile file);
    /** insert IMAGES bot db and folder */
    public List<String> storeMultiFile(MultipartFile[] files);

    /** insert image bot db and folder */
    public String uploadImageNew(MultipartFile file);
}
