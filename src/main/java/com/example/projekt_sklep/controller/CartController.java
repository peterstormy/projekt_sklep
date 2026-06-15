package com.example.projekt_sklep.controller;

import com.example.projekt_sklep.model.Order;
import com.example.projekt_sklep.model.OrderItem;
import com.example.projekt_sklep.model.Product;
import com.example.projekt_sklep.model.User;
import com.example.projekt_sklep.repository.OrderItemRepository;
import com.example.projekt_sklep.repository.OrderRepository;
import com.example.projekt_sklep.repository.ProductRepository;
import com.example.projekt_sklep.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
public class CartController {

    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private UserRepository userRepository;

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> getCart(HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return "redirect:/products";
        Map<Long, Integer> cart = getCart(session);
        int current = cart.getOrDefault(id, 0);
        if (current < product.getIlosc()) {
            cart.put(id, current + 1);
        }
        return "redirect:/products/" + id;
    }

    @PostMapping("/cart/increment/{id}")
    public String increment(@PathVariable Long id, HttpSession session) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return "redirect:/cart";
        Map<Long, Integer> cart = getCart(session);
        int current = cart.getOrDefault(id, 0);
        if (current < product.getIlosc()) {
            cart.put(id, current + 1);
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/decrement/{id}")
    public String decrement(@PathVariable Long id, HttpSession session) {
        Map<Long, Integer> cart = getCart(session);
        int current = cart.getOrDefault(id, 0);
        if (current <= 1) {
            cart.remove(id);
        } else {
            cart.put(id, current - 1);
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        Map<Long, Integer> cart = getCart(session);
        List<Product> products = productRepository.findAllById(cart.keySet());
        double total = products.stream()
                .mapToDouble(p -> p.getCena() * cart.getOrDefault(p.getId(), 0))
                .sum();
        model.addAttribute("cartItems", products);
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        getCart(session).remove(id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/cart";
    }

    @PostMapping("/cart/checkout")
    public String checkout(HttpSession session, Authentication authentication) {
        Map<Long, Integer> cart = getCart(session);
        if (cart.isEmpty()) return "redirect:/cart";

        User user = userRepository.findByLogin(authentication.getName());

        Order order = new Order();
        order.setDataZamowienia(LocalDate.now());
        order.setStatus("NOWE");
        order.setUser(user);
        Order saved = orderRepository.save(order);

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = productRepository.findById(entry.getKey()).orElse(null);
            if (product != null) {
                OrderItem item = new OrderItem();
                item.setOrder(saved);
                item.setProduct(product);
                item.setQuantity(entry.getValue());
                orderItemRepository.save(item);
                productRepository.decrementStock(entry.getKey(), entry.getValue());
            }
        }

        session.removeAttribute("cart");
        return "redirect:/orders";
    }
}
