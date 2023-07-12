package com.edu.uploadfile.mobiles.repositories;

import com.edu.uploadfile.mobiles.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    //product
    List<Category> findByName(String name);
    List<Category> findByKeyword(String keyword);
}
