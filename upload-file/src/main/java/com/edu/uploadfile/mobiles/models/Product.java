package com.edu.uploadfile.mobiles.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private boolean published;
    /**
     * show MANY to ONE
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//    @JsonBackReference      //show json response category chua product, khong lap product
    private Category category;

 //* show ONE to MANY
// @ManyToOne
// @JoinColumn(name = "category_id", nullable = false)
// @JsonBackReference
////    @JsonIgnore
//    private Category category;

    /**
     * JsonIgnoreProperties --> show category
     * JsonIgnore --> no show category
     */

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Image> images = new HashSet<>();

    public Product() {
    }

//    public Product(String name, String description, boolean published) {
//        this.name = name;
//        this.description = description;
//        this.published = published;
//    }

    public Product(String name, String description, boolean published, Category category) {
        this.name = name;
        this.description = description;
        this.published = published;
        this.category = category;
    }

//    public Product(String name, String description, boolean published, Category category, Set<Image> images) {
//        this.name = name;
//        this.description = description;
//        this.published = published;
//        this.category = category;
//        this.images = images;
//    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    //////
    public void deleteImage() {
//        this.getImages().removeAll(images);
//        images.removeAll(images);

        if (images.size() > 0) {
            System.out.println("117 *****************************************************************************");
            System.out.println("111images: " + images.size());
            this.images.removeAll(images);
            System.out.println("222images: " + images.size());
            System.out.println("333images: " + this.images.size());
            System.out.println("*119 ****************************************************************************");
        } else {
            System.out.println("image null");
        }
    }
//    public void removeImages(Image image) {
//        images.remove(image);
//        image.getProduct().deleteImage(image);
//    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", published=" + published +
                ", category=" + category +
                ", images=" + images +
                '}';
    }
}
