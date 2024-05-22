package com.akatsuki.pioms.image.repository;

import com.akatsuki.pioms.image.aggregate.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Integer> {
    List<Image> findByProductCode(int productCode);
}
