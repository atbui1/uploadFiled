package com.edu.uploadfile.mobiles.services.serviceImpl;

import com.edu.uploadfile.mobiles.models.Image;
import com.edu.uploadfile.mobiles.models.Picture;
import com.edu.uploadfile.mobiles.repositories.PictureRepository;
import com.edu.uploadfile.mobiles.services.iservice.IPictureService;
import com.edu.uploadfile.mobiles.services.iservice.IStorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class PictureServiceImp implements IPictureService {

    private final Path storageFolder = Paths.get("pictures");

    @Autowired
    PictureRepository pictureRepository;

    /** check folder is existed  */
    public PictureServiceImp() {
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
    /** rename image file upload server */
    private String renameImage(MultipartFile file) {
       try {
           String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
           String generatedFileName = UUID.randomUUID().toString().replace("-", "");
           generatedFileName = generatedFileName + "." + fileExtension;
           Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName))
                   .normalize().toAbsolutePath();
           if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
               throw new RuntimeException("cannot store file outside current directory");
           }
           try(InputStream inputStream = file.getInputStream()) {
               Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
           }
           return generatedFileName;
       } catch (IOException ex) {
           throw new RuntimeException("cannot rename image file", ex);
       }
    }

    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("********************************* initial picture file *********************************");
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store is empty");
            }
            if (!isImageFile(file)) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAA: ");
                throw new RuntimeException("you can only upload image file with extension collect \"png\", \"jpg\", \"jpeg\", \"bmp\"");
            }
//            file size <= 5MB
            float fileSizeImageBytes = file.getSize() / 1_000_000.0f;
            if (fileSizeImageBytes > 5.0f) {
                throw new RuntimeException("file must be <= 5MB");
            }
//            rename image
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();
            System.out.println("89 11111111111111111111111111111111111111111111111111111111111");
            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new RuntimeException("cannot store file outside current directory");
            }

            System.out.println("94 2222222222222222222222222222222222222222222222222222222222222");
            try(InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("98 3333333333333333333333333333333333333333333333333333333333333: " + generatedFileName);
            return generatedFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file", ex);
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            System.out.println("********************************* 108 initial storage file *********************************");
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store is empty");
            }
            if (!isImageFile(file)) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAA: ");
                throw new RuntimeException("you can only upload image file with extension collect \"png\", \"jpg\", \"jpeg\", \"bmp\"");
            }
//            file size <= 5MB
            float fileSizeImageBytes = file.getSize() / 1_000_000.0f;
            if (fileSizeImageBytes > 5.0f) {
                throw new RuntimeException("file must be <= 5MB");
            }
//            rename image
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();
            System.out.println("127 11111111111111111111111111111111111111111111111111111111111");

            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new RuntimeException("cannot store file outside current directory");
            }

            System.out.println("133 pictureImp 2222222222222222222222222222222222222222222222222222222222222");
            try(InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            Picture picture = new Picture(generatedFileName, file.getContentType(), destinationFilePath.toString(), file.getOriginalFilename());
            pictureRepository.save(picture);
            System.out.println("140 pictureImp 3333333333333333333333333333333333333333333333333333333333333: " + generatedFileName);
            return generatedFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file", ex);
        }
    }

    @Override
    public List<String> storeMultiFile(MultipartFile[] files) {
        try {
            List<String> generatedFileNames = new ArrayList<>();
            boolean checkFlag = true;
            for (MultipartFile file : files) {
                /** check request is empty*/
                if (file.isEmpty()) {
                    throw new RuntimeException("Failed to store is empty");
                }
                /** check file is image*/
                if (!isImageFile(file)) {
                    throw new RuntimeException("you can only upload image file with extension collect png, jpg, jpeg, bmp");
                }
                /** check size file*/
                float fileSizeImageBytes = file.getSize() / 1_000_000.0f;
                if (fileSizeImageBytes > 5.0f) {
                    throw new RuntimeException("file must be <= 5MB");
                }
/**change image's name
 * store image in folder
 * return list of images name
 * */
//                String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
//                String generatedFileName = UUID.randomUUID().toString().replace("-", "");
//                generatedFileName = generatedFileName + "." + fileExtension;
//                Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName))
//                        .normalize().toAbsolutePath();
//
//                if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
//                    throw new RuntimeException("cannot store file outside current directory");
//                }
//                try(InputStream inputStream = file.getInputStream()) {
//                    Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
//                }
//                generatedFileNames.add(generatedFileName);
//                Image image = new Image(generatedFileName, file.getContentType(), destinationFilePath.toString(), file.getOriginalFilename());
//                pictureRepository.save(image);

                System.out.println("111********************** checkflag: " + checkFlag);
            }
            System.out.println("222********************** checkflag: " + checkFlag);
            if(checkFlag) {
                System.out.println("11111111111111111111 co check flag: " + checkFlag);
                for (MultipartFile file:files) {
                    String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
                    String generatedFileName = UUID.randomUUID().toString().replace("-", "");
                    generatedFileName = generatedFileName + "." + fileExtension;
                    Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName))
                            .normalize().toAbsolutePath();

                    if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                        throw new RuntimeException("cannot store file outside current directory");
                    }
                    try(InputStream inputStream = file.getInputStream()) {
                        Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
                    }
                    generatedFileNames.add(generatedFileName);
//                    Image image = new Image(generatedFileName, file.getContentType(), destinationFilePath.toString(), file.getOriginalFilename());
//                    pictureRepository.save(image);
                    Picture picture = new Picture(generatedFileName, file.getContentType(), destinationFilePath.toString(), file.getOriginalFilename());
                    pictureRepository.save(picture);
                }
            }
            return generatedFileNames;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file", ex);
        }
    }

    @Override
    public Resource loadFiled(String fileName) {
        try {
            Path path = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("not read resource load file");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Not load file...!" + ex);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.storageFolder, 1)
                    .filter(path -> !path.equals(this.storageFolder) && !path.toString().contains(".-"))
                    .map(this.storageFolder::relativize);
        } catch (IOException ex) {
            throw new RuntimeException("failed to load all images in store folder ", ex);
        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            } else {
                throw new RuntimeException("could not read file: " + fileName);
            }
        } catch (IOException ex) {
            throw new RuntimeException("cannot read file " + fileName, ex);
        }
    }

    @Override
    public Picture downloadById(Long fileId) {
        return pictureRepository.findById(fileId).get();
    }
    @Override
    public Picture downloadByName(String filename) {
        return pictureRepository.findByName(filename);
    }

    @Override
    public void deleteAllFile() {

    }

    /**
     * nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn
     */
    @Override
    public Picture nStore(MultipartFile file) {
        try {
            System.out.println("********************************* initial nstorage file *********************************");
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store is empty");
            }
            if (!isImageFile(file)) {
                System.out.println("ERROR: file not a image: ");
                throw new RuntimeException("you can only upload image file with extension collect png, jpg, jpeg, bmp");
            }
//            file size <= 5MB
            float fileSizeImageBytes = file.getSize() / 1_000_000.0f;
            if (fileSizeImageBytes > 5.0f) {
                System.out.println("ERROR: larger 5MB  ");
                throw new RuntimeException("file must be <= 5MB");
            }
//            rename image
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName))
                    .normalize().toAbsolutePath();
            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new RuntimeException("cannot store file outside current directory");
            }

            try(InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("303 nStore 3333333333333333333333333333333333333333333333333333333333333: " + generatedFileName);
            Picture picture = new Picture(generatedFileName, file.getContentType(), destinationFilePath.toString(), file.getBytes().toString());
            return picture;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to nStore file", ex);
        }
    }
    @Override
    public byte[] nReadFileContent(String fileName) {
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            } else {
                throw new RuntimeException("could not read file: " + fileName);
            }
        } catch (IOException ex) {
            throw new RuntimeException("cannot read file " + fileName, ex);
        }
    }
    @Override
    public Stream<Path> nLoadAllFile() {
        try {
            return Files.walk(this.storageFolder, 1)
                    .filter(path -> !path.equals(this.storageFolder) && !path.toString().contains(".-"))
                    .map(this.storageFolder::relativize);
        } catch (IOException ex) {
            throw new RuntimeException("failed to load all images in store folder ", ex);
        }
    }

    @Override
    public Stream<Picture> nLoadAllFileImage() {
        return pictureRepository.findAll().stream();
    }
}
