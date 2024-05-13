package com.example.devconnect.repository;

import com.example.devconnect.model.PopularTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopularTagRepository extends JpaRepository<PopularTag, Long> {
}
