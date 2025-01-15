package com.example.IBOR.Cart;

import com.example.IBOR.CarPart.CarPartWithBase64Images;
import com.example.IBOR.User.User;
import com.example.IBOR.User.UserRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartController {
    private CartRepository cartRepository;
    private UserRepository userRepository;

    public CartController(CartRepository cartRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String getShowCart(Principal principal, Model model) {
        User user = userRepository.getUserByUsername(principal.getName());
        Cart cart = user.getCart();
        List<CarPartWithBase64Images> cartItemsWithImages = cart.getItems().stream().map(orderItem -> {
            List<String> base64Images = orderItem.getCarPart().getImages().stream()
                    .map(Base64::encodeBase64String)
                    .collect(Collectors.toList());
            return new CarPartWithBase64Images(orderItem.getCarPart(), base64Images);
        }).collect(Collectors.toList());
        model.addAttribute("cartItemsWithImages", cartItemsWithImages);
        model.addAttribute("cart", cart);
        return "cart/show";
    }
}