package com.example.IBOR.Cart;

import com.example.IBOR.OrderItem.OrderItem;
import com.example.IBOR.User.User;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {
    @Column(name = "cart_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public void removeItem(OrderItem item) {this.items.remove(item);}
}
