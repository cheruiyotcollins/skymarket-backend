package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Catalogue;
import com.gigster.skymarket.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogueRepository extends JpaRepository<Catalogue, Long> {

    List<Catalogue> findByCategoriesContaining(Category category);

    boolean existsByName(String name);
}
