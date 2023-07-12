package com.edu.uploadfile.mobiles.repositories;

import com.edu.uploadfile.mobiles.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByName(String name);
}
