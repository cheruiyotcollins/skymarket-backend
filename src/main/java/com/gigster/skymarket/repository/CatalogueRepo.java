package com.gigster.skymarket.repository;

import com.gigster.skymarket.model.Catalogue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogueRepo extends JpaRepository<Catalogue, Long> {
}
