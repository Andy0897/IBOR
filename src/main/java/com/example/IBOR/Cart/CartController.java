package com.example.IBOR.Cart;

import com.example.IBOR.ImageEncoder;
import com.example.IBOR.User.User;
import com.example.IBOR.User.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/cart")
public class CartController {
    private CartRepository cartRepository;
    private CartService cartService;
    private UserRepository userRepository;

    public CartController(CartRepository cartRepository, CartService cartService, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String getShowCart(Principal principal, Model model) {
        User user = userRepository.getUserByUsername(principal.getName());
        Cart cart = user.getCart();
        double cartTotalPrice = cart.getItems().stream().mapToDouble(item -> item.getTotalPrice()).sum();
        model.addAttribute("cart", cart);
        model.addAttribute("encoder", new ImageEncoder());
        model.addAttribute("cartTotalPrice", cartTotalPrice);
        return "cart/show";
    }

    @PostMapping("/submit-remove-item/{cartId}/{itemId}")
    public String submitDeleteItem(@PathVariable("cartId") Long cartId, @PathVariable("itemId") Long itemId) {
        return cartService.submitRemoveItem(cartId, itemId);
    }
}