package com.edu.uploadfile.mobiles.services.serviceImpl;

import com.edu.uploadfile.mobiles.converter.ImageConvert;
import com.edu.uploadfile.mobiles.exception.NotFoundException;
import com.edu.uploadfile.mobiles.models.Image;
import com.edu.uploadfile.mobiles.models.Product;
import com.edu.uploadfile.mobiles.payloads.requests.ImageRequest;
import com.edu.uploadfile.mobiles.repositories.ImageRepository;
import com.edu.uploadfile.mobiles.repositories.ProductRepository;
import com.edu.uploadfile.mobiles.repositories.UserRepository;
import com.edu.uploadfile.mobiles.services.iservice.IImageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ImageServiceIml implements IImageService {

    private final Path storageFolder = Paths.get("images");

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageConvert imageConvert;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;


    /** check folder is existed  */
    public ImageServiceIml() {
        try {
            Files.createDirectories(storageFolder);
        } catch (IOException ex) {
            throw new RuntimeException("cannot initialize storage folder: ", ex);
        }
    }
    /** check file is image by extension */
    private boolean isImageFile(MultipartFile file) {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[]{"png", "jpg", "jpeg", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }


    /** pool image */
    public Image validImageTest(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new NotFoundException("Failed to store is empty");
            }
            if (!isImageFile(file)) {
                throw new NotFoundException("you can only upload image file with extension collect \"png\", \"jpg\", \"jpeg\", \"bmp\"");
            }
//            file size <= 5MB
            float fileSizeImageBytes = file.getSize() / 1_000_000.0f;
            if (fileSizeImageBytes > 5.0f) {
                throw new NotFoundException("file must be <= 5MB");
            }
//            rename image
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();

            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new NotFoundException("cannot store file outside current directory");
            }

            try(InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            String baseURL = "/api/v2";
            String urlConvert = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path(baseURL)
                    .path("/image/show-image-url/")
                    .path(generatedFileName)
                    .toUriString();

            Image image = new Image(generatedFileName, file.getContentType(), destinationFilePath.toString(), urlConvert);

            return image;
        } catch (Exception ex) {
            throw new NotFoundException("Failed to store file testttttttttttttttt: " + ex);
        }
    }

    public Set<Image> validImageTestQAZ(MultipartFile[] files) {
        try {
            boolean checkFlag = true;
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    throw new NotFoundException("Failed to store is empty");
                }
                if (!isImageFile(file)) {
                    throw new NotFoundException("you can only upload image file with extension collect \"png\", \"jpg\", \"jpeg\", \"bmp\"");
                }
//            file size <= 5MB
                float fileSizeImageBytes = file.getSize() / 1_000_000.0f;
                if (fileSizeImageBytes > 5.0f) {
                    throw new NotFoundException("file must be <= 5MB");
                }
                checkFlag = true;
                System.out.println("vong lap 1");
            }
//            rename image
            Set<Image> images = new HashSet<>();
            for (MultipartFile file : files) {
                System.out.println("vong lap 2");
                String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
                String generatedFileName = UUID.randomUUID().toString().replace("-", "");
                generatedFileName = generatedFileName + "." + fileExtension;
                Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName))
                        .normalize().toAbsolutePath();

                if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                    throw new NotFoundException("cannot store file outside current directory");
                }

                try(InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
                }
                String baseURL = "/api/v2";
                String urlConvert = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path(baseURL)
                        .path("/image/show-image-url/")
                        .path(generatedFileName)
                        .toUriString();

                Image image = new Image(generatedFileName, file.getContentType(), destinationFilePath.toString(), urlConvert);
                images.add(image);
            }

            return images;
        } catch (Exception ex) {
            throw new NotFoundException("Failed to store file testttttttttttttttt: " + ex);
        }
    }
    public Image validImageMain(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new NotFoundException("Failed to store is empty");
            }
            if (!isImageFile(file)) {
                throw new NotFoundException("you can only upload image file with extension collect \"png\", \"jpg\", \"jpeg\", \"bmp\"");
            }
//            file size <= 5MB
            float fileSizeImageBytes = file.getSize() / 1_000_000.0f;
            if (fileSizeImageBytes > 5.0f) {
                throw new NotFoundException("file must be <= 5MB");
            }
//            rename image
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();

            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new NotFoundException("cannot store file outside current directory");
            }

            try(InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            String baseURL = "/api/v2";
            String urlConvert = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path(baseURL)
                    .path("/image/show-image-url/")
                    .path(generatedFileName)
//                    .path(x.getName())
                    .toUriString();

            Image image = new Image(generatedFileName, file.getContentType(), destinationFilePath.toString(), urlConvert);
//            Image image = new Image(generatedFileName, file.getContentType(), destinationFilePath.toString(), file.getOriginalFilename());

            return image;
        } catch (Exception ex) {
            throw new NotFoundException("Failed to store file: " + ex);
        }
    }

    @Override
    public String uploadOneImage(MultipartFile file, Long id) {
        Image image = validImageMain(file);
        System.out.println("**************** end valid img *******************************");
        Product product = productRepository.findById(id).get();
        System.out.println("116 product: " + product);
        image.setProduct(product);
        Set<Image> images = new HashSet<>();
        images.add(image);
//        product.setImages(images);

        imageRepository.save(image);
        return "99999999999999999999999999999 /////////////////// *******************************";
    }

    @Override
    public String uploadManyImages(MultipartFile[] files, Long id) {
        try {
            Set<Image> images = new HashSet<>();
            Image image = new Image();
            Product product = productRepository.findById(id).get();
            System.out.println("start product 166: " + product);
            boolean checkFlag = true;
            for (MultipartFile file : files) {
                image = validImageMain(file);
                image.setProduct(product);
                images.add(image);
                System.out.println("for checkFlag: " + checkFlag);
            }
//            product.setImages(images);
            System.out.println("images 174: " + images);
            System.out.println("product 175: " + product);
            System.out.println("total checkFlag: " + checkFlag);
            if (checkFlag) {
                for (Image x : images) {
                    imageRepository.save(x);
                    System.out.println("177 uploadManyImages luu file roi *******************************************************");
                }
                System.out.println("product with images182: " + product);
//                product.setImages(images);
                System.out.println("product with images184: " + product);
            }
//            product.setImages(images);
            System.out.println("product 184: " + product);
        } catch (Exception ex) {
            throw new NotFoundException("exist file not image");
        }
//        return null;
        return "a";
    }

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new NotFoundException("Failed to store is empty");
            }
            if (!isImageFile(file)) {
                throw new NotFoundException("you can only upload image file with extension collect \"png\", \"jpg\", \"jpeg\", \"bmp\"");
            }
//            file size <= 5MB
            float fileSizeImageBytes = file.getSize() / 1_000_000.0f;
            if (fileSizeImageBytes > 5.0f) {
                throw new NotFoundException("file must be <= 5MB");
            }
//            rename image
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();

            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new NotFoundException("cannot store file outside current directory");
            }

            try(InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }

            Image image = new Image(generatedFileName, file.getContentType(), destinationFilePath.toString(), file.getOriginalFilename());
//            imageRepository.save(image);
            System.out.println("103 ImageServiceIml image name: " + generatedFileName);
            System.out.println("104 ImageServiceIml image url: " + destinationFilePath);
            return generatedFileName;
        } catch (IOException ex) {
//            throw new RuntimeException("Failed to store file", ex);
            throw new NotFoundException("Failed to store file: " + ex);
        }
    }

    @Override
    public String uploadImageById(MultipartFile file, Long id) {
        try {
            System.out.println("********************************* 67 initial storage file ImageServiceIml*********************************");
            if (file.isEmpty()) {
//                throw new RuntimeException("Failed to store is empty");
                throw new NotFoundException("Failed to store is empty");
            }
            System.out.println("********************************* 72 ImageServiceIml*********************************");
            if (!isImageFile(file)) {
//                throw new RuntimeException("you can only upload image file with extension collect \"png\", \"jpg\", \"jpeg\", \"bmp\"");
                throw new NotFoundException("you can only upload image file with extension collect \"png\", \"jpg\", \"jpeg\", \"bmp\"");
            }
            System.out.println("********************************* 77 ImageServiceIml*********************************");
//            file size <= 5MB
            float fileSizeImageBytes = file.getSize() / 1_000_000.0f;
            if (fileSizeImageBytes > 5.0f) {
//                throw new RuntimeException("file must be <= 5MB");
                throw new NotFoundException("file must be <= 5MB");
            }
//            rename image
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();
            System.out.println("86 ImageServiceIml 11111111111111111111111111111111111111111111111111111111111");

            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
//                throw new RuntimeException("cannot store file outside current directory");
                throw new NotFoundException("cannot store file outside current directory");
            }

            System.out.println("93 ImageServiceIml 2222222222222222222222222222222222222222222222222222222222222");
            try(InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            Optional<Product> product = productRepository.findById(id);
            Product productOfImage = product.get();
            Image image = new Image(generatedFileName, file.getContentType(),
                    destinationFilePath.toString(), file.getOriginalFilename(),
                    productOfImage);

//            Image image = new Image(generatedFileName, file.getContentType(), destinationFilePath.toString(), file.getOriginalFilename());
            System.out.println("103 ImageServiceIml image dto: " + image);
            System.out.println("103 ImageServiceIml image file: " + file);
            System.out.println("103 ImageServiceIml image rename: " + generatedFileName);
            System.out.println("103 ImageServiceIml image type: " + file.getContentType());
            System.out.println("103 ImageServiceIml image url local: " + destinationFilePath);
            System.out.println("104 ImageServiceIml image url: " + file.getOriginalFilename());
            System.out.println("104 ImageServiceIml image product: " + productOfImage);
            imageRepository.save(image);
            System.out.println("EEEEEEEEEEEEEEEEE------------------------------EEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
//            return image;
//            return generatedFileName;
            return "SUCCESS: 999999999999999999999-------------------9999999999999999999999999";
        } catch (IOException ex) {
//            throw new RuntimeException("Failed to store file", ex);
            throw new NotFoundException("Failed to store file: " + ex);
        }
    }

    @Override
    public Image getImageFormData(MultipartFile file) {
        try {
            System.out.println("********************************* 67 initial storage file ImageServiceIml*********************************");
            if (file.isEmpty()) {
//                throw new RuntimeException("Failed to store is empty");
                throw new NotFoundException("Failed to store is empty");
            }
            System.out.println("********************************* 72 ImageServiceIml*********************************");
            if (!isImageFile(file)) {
//                throw new RuntimeException("you can only upload image file with extension collect \"png\", \"jpg\", \"jpeg\", \"bmp\"");
                throw new NotFoundException("you can only upload image file with extension collect \"png\", \"jpg\", \"jpeg\", \"bmp\"");
            }
            System.out.println("********************************* 77 ImageServiceIml*********************************");
//            file size <= 5MB
            float fileSizeImageBytes = file.getSize() / 1_000_000.0f;
            if (fileSizeImageBytes > 5.0f) {
//                throw new RuntimeException("file must be <= 5MB");
                throw new NotFoundException("file must be <= 5MB");
            }
//            rename image
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();
            System.out.println("86 ImageServiceIml 11111111111111111111111111111111111111111111111111111111111");

            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
//                throw new RuntimeException("cannot store file outside current directory");
                throw new NotFoundException("cannot store file outside current directory");
            }

            System.out.println("93 ImageServiceIml 2222222222222222222222222222222222222222222222222222222222222");
            try(InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            Image image = new Image(generatedFileName, file.getContentType(), destinationFilePath.toString(), file.getOriginalFilename());
//            imageRepository.save(image);
            System.out.println("103 ImageServiceIml image dto: " + image);
            System.out.println("103 ImageServiceIml image name: " + generatedFileName);
            System.out.println("104 ImageServiceIml image url: " + destinationFilePath);
            return image;
//            return generatedFileName;
        } catch (IOException ex) {
//            throw new RuntimeException("Failed to store file", ex);
            throw new NotFoundException("Failed to store file: " + ex);
        }
    }

    /** show image on url*/
    @Override
    public byte[] readFileContent(String filename) {
        try {
            Path file = storageFolder.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            } else {
                throw new NotFoundException("could not read/show file 11: " + filename +
                        "\n resource: " + resource);
            }

        } catch (IOException ex) {
            System.out.println("9999999999999999999999****************999999999999999999999999999999999");
            throw new NotFoundException("cannot read/show file 22:" + filename + "\n new IOException:" + ex);
        }
    }

    @Override
    public Stream<ImageRequest> nLoadAllFileImage() {
        System.out.println("11111111111111111111111111111111111111111111111111111111");
        String baseURL = "/api/v2";
        List<ImageRequest> imageRequests = new ArrayList<>();
        ImageRequest imageRequest = new ImageRequest();
        List<Image> images = imageRepository.findAll();
        if (images.size() < 1) {
            throw new NotFoundException("khong co img");
        }
        System.out.println("2222222222222222222222222222222222222222222222222");
        for (Image x: images) {

            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path(baseURL)
                    .path("/image/show-image-url/")
                    .path(x.getName())
                    .toUriString();
            imageRequest = imageConvert.toDTo(x);
            imageRequest.setUrl(fileDownloadUri);
            imageRequests.add(imageRequest);
        }
        System.out.println("xyx: " + imageRequests);
        System.out.println("333333333333333333333333333333333333333333333333333333333");
        return imageRequests.stream();
    }

    @Override
    public void deleteFileFolder(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        if (!image.isPresent()) {
            System.out.println("khong ton tai image");
            throw new NotFoundException("khong co image");
        }
        String imageName = image.get().getName();
        String pathTmp = storageFolder + ":\\\\" + imageName;
        System.out.println("pathTMP: " + pathTmp);
        System.out.println("image name: " + imageName);
        File file = new File(pathTmp);
        if (file.exists()) {
//        if (file.exists() && file.delete()) {
            System.out.println("File deleted successfully");
        }
//        imageRepository.deleteById(id);
    }
}
