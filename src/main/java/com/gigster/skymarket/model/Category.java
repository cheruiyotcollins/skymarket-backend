package com.gigster.skymarket.model;

import com.gigster.skymarket.enums.CategoryName;
import com.gigster.skymarket.utils.DateUtils;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private CategoryName categoryName;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, updatable = false)
    private String createdDate = DateUtils.dateNowString();

    @Column(nullable = false)
    private String updatedDate = DateUtils.dateNowString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
}
