package com.example.IBOR.Order;

import com.example.IBOR.Cart.Cart;
import com.example.IBOR.Cart.CartRepository;
import com.example.IBOR.User.User;
import com.example.IBOR.User.UserRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public String submitCheckout(Order order, BindingResult bindingResult, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("order", order);
            return "order/checkout";
        }
        User user = userRepository.getUserByUsername(principal.getName());
        Cart cart = user.getCart();
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setStatus("В изчакване");
        order.setTotalPrice(cart.getItems().stream().mapToDouble(item -> item.getTotalPrice()).sum());
        orderRepository.save(order);

        cart.setItems(new ArrayList<>());
        cartRepository.save(cart);
        return "redirect:/home";
    }

    public String submitUpdateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).get();
        order.setStatus(status);
        orderRepository.save(order);
        return "redirect:/orders/show-all";
    }
}