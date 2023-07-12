package com.edu.uploadfile.mobiles.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String keyword;

    /**
     * show one to many
     *
     * @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
     * @JsonManagedReference
     * private Set<Product> products = new HashSet<>();
     * show json response chua product
     */
 //     @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
 //     @JsonManagedReference
//      @JsonIgnore
  //    private Set<Product> products = new HashSet<>();
    public Category() {
    }

    public Category(String name, String keyword) {
        this.name = name;
        this.keyword = keyword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyword() {
        return keyword.toUpperCase();
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword.toUpperCase();
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
