package com.edu.uploadfile.mobiles.services.iservice;

import com.edu.uploadfile.mobiles.models.Image;
import com.edu.uploadfile.mobiles.models.Picture;
import com.edu.uploadfile.mobiles.payloads.requests.ImageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Stream;

public interface IImageService {

    String uploadOneImage(MultipartFile file, Long id);
    String uploadManyImages(MultipartFile[] files, Long id);
    String uploadImage(MultipartFile file);
    String uploadImageById(MultipartFile file, Long id);

    Image getImageFormData(MultipartFile file);

    /** show image on url */
    public byte[] readFileContent(String filename);

    /** load all images */
    public Stream<ImageRequest> nLoadAllFileImage();

    void deleteFileFolder(Long id);
}
