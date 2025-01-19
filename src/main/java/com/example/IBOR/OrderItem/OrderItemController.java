package com.example.IBOR.OrderItem;

import com.example.IBOR.Order.Order;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String getSubmitOrderItem(@Valid @ModelAttribute("orderItem") OrderItemDTO orderItemDTO, BindingResult bindingResult, @PathVariable("carPartId") Long carPartId, Principal principal, Model model) {
        return orderItemService.submitOrderItem(orderItemDTO, bindingResult, carPartId, principal, model);
    }
}
