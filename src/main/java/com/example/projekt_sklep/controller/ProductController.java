package com.example.projekt_sklep.controller;

import com.example.projekt_sklep.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.projekt_sklep.model.Product;
import org.springframework.web.bind.annotation.PostMapping;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products")
    public String showProducts(Model model) {

        model.addAttribute("products", productRepository.findAll());

        return "products";
    }

    @GetMapping("/products/add")
    public String addProductForm(Model model) {

        model.addAttribute("product", new Product());

        return "addProduct";
    }

    @PostMapping("/products/save")
    public String saveProduct(@Valid Product product,
                              BindingResult result) {

        if (result.hasErrors()) {
            return "addProduct";
        }

        product.setDataDodania(LocalDate.now());

        productRepository.save(product);

        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model){

        Product product = productRepository.findById(id).orElse(null);

        model.addAttribute("product", product);

        return "editProduct";
    }

    @PostMapping("/products/update")
    public String updateProduct(Product product){

        productRepository.save(product);

        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id){

        productRepository.deleteById(id);

        return "redirect:/products";
    }
}