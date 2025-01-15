package com.example.IBOR.OrderItem;

import com.example.IBOR.CarPart.CarPart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
    public OrderItem findByCarPart(CarPart carPart);
}