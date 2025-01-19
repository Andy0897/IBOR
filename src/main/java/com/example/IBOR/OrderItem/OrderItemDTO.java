package com.example.IBOR.OrderItem;

import com.example.IBOR.CarPart.CarPart;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class OrderItemDTO {
    private CarPart carPart;
    @Min(value = 1, message = "Броят трябва да бъде поне 1.")
    @Max(value = 10, message = "Броят не трябва да надвишава 10.")
    private int quantity;

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
}
