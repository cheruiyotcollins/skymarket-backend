package com.gigster.skymarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    // Todo move this to service
    public void addItem(CartItem item) {
        this.cartItems.add(item);
        item.setCart(this); // Set back reference to the cart
    }

    public void removeItem(CartItem item) {
        this.cartItems.remove(item);
        item.setCart(null); // Remove the cart reference
    }

    public double getTotalPrice() {
        return cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
    }
}

