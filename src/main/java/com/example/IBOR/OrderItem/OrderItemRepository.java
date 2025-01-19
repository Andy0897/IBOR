package com.example.IBOR.OrderItem;

import com.example.IBOR.CarPart.CarPart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
    public OrderItem findByCarPart(CarPart carPart);

    @Query("SELECT oi FROM Cart c JOIN c.items oi WHERE oi.carPart.id = :carPartId")
    OrderItem findOrderItemByCarPartId(@Param("carPartId") Long carPartId);

    @Query(value = "SELECT oi.* FROM order_items oi " +
            "JOIN car_parts cp ON cp.car_part_id = oi.car_part_id " +
            "JOIN carts_items ci ON ci.items_order_item_id = oi.order_item_id " +
            "JOIN carts c ON c.cart_id = ci.cart_cart_id " +
            "WHERE c.cart_id = :cartId AND cp.car_part_id = :carPartId",
            nativeQuery = true)
    public OrderItem findOrderItemInCartByCarPartId(@Param("cartId") Long cartId, @Param("carPartId") Long carPartId);

}