package com.example.IBOR.Cart;

import com.example.IBOR.CarPart.CarPart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends CrudRepository<Cart, Long> {
}