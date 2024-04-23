package com.example.devconnect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "image")
public class Image {
    @Id
    @ColumnDefault("nextval('image_id_seq'")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}