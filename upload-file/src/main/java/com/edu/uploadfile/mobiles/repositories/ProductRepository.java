package com.edu.uploadfile.mobiles.repositories;

import com.edu.uploadfile.mobiles.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //image
    List<Product> findByPublished (boolean published);
    List<Product> findByNameContaining(String name);
    List<Product> findByName(String name);
    Boolean existsByName(String name);
    //category
    List<Product> findByCategoryId(Long categoryId);
    @Transactional
    void deleteByCategoryId(long categoryId);
}
