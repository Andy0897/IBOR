package com.example.IBOR.OrderItem;

import com.example.IBOR.CarPart.CarPart;
import com.example.IBOR.CarPart.CarPartRepository;
import com.example.IBOR.Cart.Cart;
import com.example.IBOR.Cart.CartRepository;
import com.example.IBOR.ImageEncoder;
import com.example.IBOR.User.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;

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

    public String submitOrderItem(OrderItemDTO orderItemDTO, BindingResult bindingResult, Long carPartId, Principal principal, Model model) {
        CarPart carPart = carPartRepository.findById(carPartId).get();
        orderItemDTO.setCarPart(carPart);
        Cart cart = userRepository.getUserByUsername(principal.getName()).getCart();

        if (bindingResult.hasErrors() || orderItemDTO.getQuantity() > carPart.getQuantity()) {
            model.addAttribute("carPart", carPart);
            model.addAttribute("encoder", new ImageEncoder());
            model.addAttribute("orderItem", orderItemDTO);
            model.addAttribute("invalidQuantity", orderItemDTO.getQuantity() > carPart.getQuantity());
            return "car-part/showSingle";
        }
        if (orderItemRepository.findOrderItemInCartByCarPartId(cart.getId(), carPart.getId()) != null) {
            OrderItem currentOrderItem = orderItemRepository.findOrderItemInCartByCarPartId(cart.getId(), orderItemDTO.getCarPart().getId());
            currentOrderItem.setQuantity(currentOrderItem.getQuantity() + orderItemDTO.getQuantity());
            currentOrderItem.setTotalPrice(orderItemDTO.getCarPart().getPrice() * currentOrderItem.getQuantity());
            carPart.setQuantity(carPart.getQuantity() - orderItemDTO.getQuantity());

            orderItemRepository.save(currentOrderItem);
        } else {
            OrderItem orderItem = new OrderItem();

            orderItem.setTotalPrice(orderItemDTO.getQuantity() * orderItemDTO.getCarPart().getPrice());
            orderItem.setCarPart(orderItemDTO.getCarPart());
            orderItem.setQuantity(orderItemDTO.getQuantity());
            cart.addItem(orderItem);
            carPart.setQuantity(carPart.getQuantity() - orderItem.getQuantity());

            orderItemRepository.save(orderItem);
            cartRepository.save(cart);
        }
        carPartRepository.save(carPart);
        return "redirect:/car-parts/show/" + orderItemDTO.getCarPart().getId();
    }
}