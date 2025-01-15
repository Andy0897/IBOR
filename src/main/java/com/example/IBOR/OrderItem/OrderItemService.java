package com.example.IBOR.OrderItem;

import com.example.IBOR.CarPart.CarPart;
import com.example.IBOR.CarPart.CarPartRepository;
import com.example.IBOR.CarPart.CarPartWithBase64Images;
import com.example.IBOR.Cart.Cart;
import com.example.IBOR.Cart.CartRepository;
import com.example.IBOR.User.User;
import com.example.IBOR.User.UserRepository;
import jakarta.validation.Valid;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    private OrderItemRepository orderItemRepository;
    private CarPartRepository carPartRepository;
    private UserRepository userRepository;
    private CartRepository cartRepository;

    public OrderItemService(OrderItemRepository orderItemRepository, CarPartRepository carPartRepository, UserRepository userRepository, CartRepository cartRepository) {
        this.orderItemRepository = orderItemRepository;
        this.carPartRepository = carPartRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public String submitOrderItem(OrderItem orderItem, Long carPartId, BindingResult bindingResult, Principal principal, Model model) {
        CarPart carPart = carPartRepository.findById(carPartId).get();
        orderItem.setCarPart(carPart);
        if(bindingResult.hasErrors()) {
            List<String> base64Images = orderItem.getCarPart().getImages().stream()
                    .map(Base64::encodeBase64String)
                    .collect(Collectors.toList());
            CarPartWithBase64Images carPartWithImages = new CarPartWithBase64Images(orderItem.getCarPart(), base64Images);
            model.addAttribute("carPartWithImages", carPartWithImages);
            model.addAttribute("orderItem", orderItem);
            return "car-part/showSingle";
        }
        if(orderItemRepository.findByCarPart(orderItem.getCarPart()) != null) {
            OrderItem currentOrderItem = orderItemRepository.findByCarPart(orderItem.getCarPart());
            currentOrderItem.setQuantity(currentOrderItem.getQuantity() + orderItem.getQuantity());
            orderItem.setTotalPrice(orderItem.getCarPart().getPrice() * currentOrderItem.getQuantity());
            orderItemRepository.save(currentOrderItem);
        }
        else {
            orderItem.setTotalPrice(orderItem.getCarPart().getPrice() * orderItem.getQuantity());
            orderItemRepository.save(orderItem);
            User user = userRepository.getUserByUsername(principal.getName());
            Cart cart = user.getCart();
            cart.addItem(orderItem);
            cartRepository.save(cart);
        }
        return "redirect:/car-parts/show/" + orderItem.getCarPart().getId();
    }
}