package com.example.IBOR.Order;

import com.example.IBOR.Cart.Cart;
import com.example.IBOR.OrderItem.OrderItem;
import com.example.IBOR.User.User;
import com.example.IBOR.User.UserRepository;
import jakarta.validation.Valid;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private OrderRepository orderRepository;
    private OrderService orderService;
    private UserRepository userRepository;

    public OrderController(OrderRepository orderRepository, OrderService orderService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @GetMapping("/show-all")
    public String getAllOrders(Model model) {
        List<Order> orders = (List<Order>) orderRepository.findAll();
        Collections.reverse(orders);
        model.addAttribute("orders", orders);
        return "order/showAll";
    }

    @GetMapping("/checkout")
    public String getCheckout(Cart cart, Model model) {
        List<OrderItem> items = cart.getItems();
        Order order = new Order();
        order.setItems(items);
        model.addAttribute("order", order);
        return "order/checkout";
    }

    @PostMapping("/submit-checkout")
    public String getSubmitCheckout(@Valid Order order, BindingResult bindingResult, Principal principal, Model model) {
        return orderService.submitCheckout(order, bindingResult, principal, model);
    }

    @PostMapping("/submit-update-status/{orderId}")
    public String getSubmitUpdateStatus(@PathVariable("orderId") Long orderId, @RequestParam("status") String status) {
        return orderService.submitUpdateStatus(orderId, status);
    }

    @GetMapping("/profile-orders")
    public String getShowProfileOrders(String name, Principal principal, Model model) {
        User user = userRepository.getUserByUsername(principal.getName());
        List<Order> orders = orderRepository.findOrdersByUser(user);
        Collections.reverse(orders);
        model.addAttribute("orders", orders);
        return "order/profileOrders";
    }
}
