package com.shop.repository;

import com.shop.entitty.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.entitty.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    List<Category> findByStatusTrue();
    Boolean existsByCategoryName(String name);
}
