package com.example.projekt_sklep.controller;

import com.example.projekt_sklep.model.Order;
import com.example.projekt_sklep.model.OrderItem;
import com.example.projekt_sklep.model.Product;
import com.example.projekt_sklep.model.User;
import com.example.projekt_sklep.repository.OrderItemRepository;
import com.example.projekt_sklep.repository.OrderRepository;
import com.example.projekt_sklep.repository.ProductRepository;
import com.example.projekt_sklep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    private boolean isAdminOrEmployee(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_EMPLOYEE"));
    }

    @GetMapping("/orders")
    public String showOrders(Model model, Authentication auth,
                              @RequestParam(required = false) String search) {
        if (isAdminOrEmployee(auth)) {
            List<Order> orders = orderRepository.findAll(Sort.by("id").descending());
            if (search != null && !search.isBlank()) {
                String q = search.toLowerCase();
                orders = orders.stream()
                        .filter(o -> o.getUser() != null && (
                                o.getUser().getImie().toLowerCase().contains(q) ||
                                o.getUser().getNazwisko().toLowerCase().contains(q) ||
                                o.getUser().getLogin().toLowerCase().contains(q)))
                        .collect(Collectors.toList());
            }
            model.addAttribute("orders", orders);
            model.addAttribute("isAdmin", true);
        } else {
            User user = userRepository.findByLogin(auth.getName());
            model.addAttribute("orders", orderRepository.findByUserOrderByIdDesc(user));
            model.addAttribute("isAdmin", false);
        }
        model.addAttribute("search", search);
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model, Authentication auth) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return "redirect:/orders";
        if (!isAdminOrEmployee(auth)) {
            User user = userRepository.findByLogin(auth.getName());
            if (order.getUser() == null || !order.getUser().getId().equals(user.getId()))
                return "redirect:/orders";
        }
        double total = order.getItems().stream()
                .mapToDouble(i -> i.getProduct().getCena() * i.getQuantity()).sum();
        model.addAttribute("order", order);
        model.addAttribute("total", total);
        model.addAttribute("isAdmin", isAdminOrEmployee(auth));
        return "orderDetail";
    }

    @GetMapping("/orders/add")
    public String addOrder(Model model) {
        Order order = new Order();
        order.setDataZamowienia(LocalDate.now());
        model.addAttribute("order", order);
        return "addOrder";
    }

    @PostMapping("/orders/save")
    public String saveOrder(@RequestParam String dataZamowienia,
                            @RequestParam String status) {
        Order order = new Order();
        order.setDataZamowienia(LocalDate.parse(dataZamowienia));
        order.setStatus(status);
        Order saved = orderRepository.save(order);
        return "redirect:/orders/edit/" + saved.getId();
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
        return "redirect:/orders";
    }

    @GetMapping("/orders/edit/{id}")
    public String editOrder(@PathVariable Long id, Model model,
                            @RequestParam(required = false) String productSearch) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return "redirect:/orders";

        Set<Long> inOrderIds = order.getItems().stream()
                .map(i -> i.getProduct().getId()).collect(Collectors.toSet());

        List<Product> available = productRepository.findAll();
        if (productSearch != null && !productSearch.isBlank()) {
            String q = productSearch.toLowerCase();
            available = available.stream()
                    .filter(p -> p.getNazwa().toLowerCase().contains(q) ||
                                 p.getProducent().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }
        List<Product> productsToAdd = available.stream()
                .filter(p -> !inOrderIds.contains(p.getId()))
                .collect(Collectors.toList());

        double orderTotal = order.getItems().stream()
                .mapToDouble(i -> i.getProduct().getCena() * i.getQuantity()).sum();

        model.addAttribute("order", order);
        model.addAttribute("productsToAdd", productsToAdd);
        model.addAttribute("productSearch", productSearch);
        model.addAttribute("orderTotal", orderTotal);
        return "editOrder";
    }

    @PostMapping("/orders/update")
    public String updateOrder(@RequestParam Long id,
                              @RequestParam String dataZamowienia,
                              @RequestParam String status) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setDataZamowienia(LocalDate.parse(dataZamowienia));
            order.setStatus(status);
            orderRepository.save(order);
        }
        return "redirect:/orders/edit/" + id;
    }

    @PostMapping("/orders/edit/{id}/addProduct/{productId}")
    public String addProductToOrder(@PathVariable Long id, @PathVariable Long productId,
                                    @RequestParam(required = false) String productSearch) {
        Order order = orderRepository.findById(id).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);
        if (order != null && product != null) {
            orderItemRepository.findByOrderIdAndProductId(id, productId).ifPresentOrElse(
                    item -> { item.setQuantity(item.getQuantity() + 1); orderItemRepository.save(item); },
                    () -> {
                        OrderItem item = new OrderItem();
                        item.setOrder(order);
                        item.setProduct(product);
                        item.setQuantity(1);
                        orderItemRepository.save(item);
                    }
            );
        }
        String redirect = "redirect:/orders/edit/" + id;
        if (productSearch != null && !productSearch.isBlank())
            redirect += "?productSearch=" + productSearch;
        return redirect;
    }

    @PostMapping("/orders/edit/{id}/incrementItem/{itemId}")
    public String incrementItem(@PathVariable Long id, @PathVariable Long itemId) {
        orderItemRepository.findById(itemId).ifPresent(item -> {
            item.setQuantity(item.getQuantity() + 1);
            orderItemRepository.save(item);
        });
        return "redirect:/orders/edit/" + id;
    }

    @PostMapping("/orders/edit/{id}/decrementItem/{itemId}")
    public String decrementItem(@PathVariable Long id, @PathVariable Long itemId) {
        orderItemRepository.findById(itemId).ifPresent(item -> {
            if (item.getQuantity() <= 1) {
                orderItemRepository.delete(item);
            } else {
                item.setQuantity(item.getQuantity() - 1);
                orderItemRepository.save(item);
            }
        });
        return "redirect:/orders/edit/" + id;
    }

    @PostMapping("/orders/edit/{id}/removeItem/{itemId}")
    public String removeItem(@PathVariable Long id, @PathVariable Long itemId) {
        orderItemRepository.deleteById(itemId);
        return "redirect:/orders/edit/" + id;
    }
}
