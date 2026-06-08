package com.example.projekt_sklep.controller;

import com.example.projekt_sklep.model.Order;
import com.example.projekt_sklep.repository.OrderRepository;
import com.example.projekt_sklep.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/orders")
    public String showOrders(Model model){

        model.addAttribute("orders",
                orderRepository.findAll());

        return "orders";
    }

    @GetMapping("/orders/add")
    public String addOrder(Model model){

        Order order = new Order();
        order.setDataZamowienia(LocalDate.now());

        model.addAttribute("order", order);
        model.addAttribute("products",
                productRepository.findAll());

        return "addOrder";
    }
    @PostMapping("/orders/save")
    public String saveOrder(Order order){

        orderRepository.save(order);

        return "redirect:/orders";
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable Long id){

        orderRepository.deleteById(id);

        return "redirect:/orders";
    }

    @GetMapping("/orders/edit/{id}")
    public String editOrder(@PathVariable Long id,
                            Model model){

        Order order =
                orderRepository.findById(id).orElse(null);

        model.addAttribute("order", order);
        model.addAttribute("products",
                productRepository.findAll());

        return "editOrder";
    }

    @PostMapping("/orders/update")
    public String updateOrder(Order order){

        orderRepository.save(order);

        return "redirect:/orders";
    }

}