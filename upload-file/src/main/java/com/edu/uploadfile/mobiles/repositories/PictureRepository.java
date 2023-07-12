package com.edu.uploadfile.mobiles.repositories;

import com.edu.uploadfile.mobiles.models.Image;
import com.edu.uploadfile.mobiles.models.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    Picture findByName(String name);
}
