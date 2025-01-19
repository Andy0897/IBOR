package com.example.IBOR.OrderItem;

import com.example.IBOR.CarPart.CarPart;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Column(name = "order_item_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_part_id")
    private CarPart carPart;

    private int quantity;

    private double totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CarPart getCarPart() {
        return carPart;
    }

    public void setCarPart(CarPart carPart) {
        this.carPart = carPart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}