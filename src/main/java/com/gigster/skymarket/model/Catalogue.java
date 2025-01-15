package com.gigster.skymarket.model;

import com.gigster.skymarket.utils.DateUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "catalogues")
public class Catalogue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long catalogueId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 500)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "catalogue_categories",
            joinColumns = @JoinColumn(name = "catalogue_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "catalogue_products",
            joinColumns = @JoinColumn(name = "catalogue_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    @Column(name = "created_on")
    @Builder.Default
    private String createdOn = DateUtils.dateNowString();

    @Column(name = "updated_at")
    @Builder.Default
    private String updatedAt = DateUtils.dateNowString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
}
