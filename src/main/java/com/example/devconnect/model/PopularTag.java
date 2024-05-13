package com.example.devconnect.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "popular_tags")
public class PopularTag {
    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "tag_count")
    private Long tagCount;

    public String getName() {
        return name;
    }

    public Long getTagCount() {
        return tagCount;
    }

    protected PopularTag() {
    }
}