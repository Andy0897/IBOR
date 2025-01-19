package com.example.IBOR.CarPart;

import com.example.IBOR.OrderItem.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface CarPartRepository extends CrudRepository<CarPart, Long> {
}
