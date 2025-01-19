package com.example.IBOR.Order;

import com.example.IBOR.Cart.CartRepository;
import com.example.IBOR.User.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    public List<Order> findOrdersByUser(User user);
}
