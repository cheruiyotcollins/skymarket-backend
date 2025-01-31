package com.gigster.skymarket.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate the user_id
    @Column(name = "user_id") // Maps to the user_id column in the database
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY) // Establish a relationship with the Customer entity
    @JoinColumn(name = "customer_id") // Map customer_id in the User table to customer_id in the Customer table
    private Customer customer;

    @Column(name = "customer_id", insertable = false, updatable = false) // Prevent duplication
    private Long customerId;

    @NotBlank
    @Size(max = 40)
    private String fullName;

    @NotBlank
    @Size(max = 15)
    private String username;

    @NotBlank
    private String contact;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    private String gender;

    @NotBlank
    @Size(max = 100)
    private String password;

    private String publicId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Column(nullable = false)
    private Boolean firstLogin = true;

    private String resetCode;

    private LocalDateTime resetCodeExpiry;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    public boolean hasRole(String roleName) {
        roles.stream().anyMatch(role -> false);
        return true;
    }
}
