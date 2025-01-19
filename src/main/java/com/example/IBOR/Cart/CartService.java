package com.example.IBOR.Cart;

import com.example.IBOR.CarPart.CarPart;
import com.example.IBOR.CarPart.CarPartRepository;
import com.example.IBOR.OrderItem.OrderItem;
import com.example.IBOR.OrderItem.OrderItemRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private CartRepository cartRepository;
    private OrderItemRepository orderItemRepository;
    private CarPartRepository carPartRepository;

    public CartService(CartRepository cartRepository, OrderItemRepository orderItemRepository, CarPartRepository carPartRepository) {
        this.cartRepository = cartRepository;
        this.orderItemRepository = orderItemRepository;
        this.carPartRepository = carPartRepository;
    }

    public String submitRemoveItem(Long cartId, Long itemId) {
        Cart cart = cartRepository.findById(cartId).get();
        OrderItem orderItem = orderItemRepository.findById(itemId).get();
        cart.removeItem(orderItem);
        CarPart carPart = orderItem.getCarPart();
        carPart.setQuantity(carPart.getQuantity() + orderItem.getQuantity());

        carPartRepository.save(carPart);
        cartRepository.save(cart);
        orderItemRepository.delete(orderItem);
        return "redirect:/cart/";
    }
}
