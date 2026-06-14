package com.example.projekt_sklep.controller;

import com.example.projekt_sklep.model.Category;
import com.example.projekt_sklep.repository.CategoryRepository;
import com.example.projekt_sklep.repository.ProductRepository;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/categories")
    public String showCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "categories";
    }

    @GetMapping("/categories/{id}")
    public String showCategory(@PathVariable Long id, Model model) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) return "redirect:/categories";
        model.addAttribute("category", category);
        model.addAttribute("products", productRepository.findByCategoryId(id));
        return "categoryDetail";
    }

    @GetMapping("/categories/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "addCategory";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "addCategory";
        }
        if (category.getDataDodania() == null) {
            category.setDataDodania(LocalDate.now());
        }
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryRepository.findById(id).orElse(null));
        return "editCategory";
    }

    @PostMapping("/categories/update")
    public String updateCategory(@Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "editCategory";
        }
        categoryRepository.save(category);
        return "redirect:/categories";
    }
}
