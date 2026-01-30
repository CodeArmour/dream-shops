package com.dailycode.dreamshops.repository;

import com.dailycode.dreamshops.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    Optional<Category> findByNameIgnoreCase(String name);
    boolean existsByName(String name);
}
