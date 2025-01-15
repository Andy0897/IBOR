package com.example.IBOR.OrderItem;

import com.example.IBOR.CarPart.CarPartWithBase64Images;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/order-items")
public class OrderItemController {
    private OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping("/submit/{carPartId}")
    public String getSubmitOrderItem(@Valid @ModelAttribute("orderItem") OrderItem orderItem, BindingResult bindingResult, @PathVariable("carPartId") Long carPartId, Principal principal, Model model) {
        return orderItemService.submitOrderItem(orderItem, carPartId, bindingResult, principal, model);
    }
}
