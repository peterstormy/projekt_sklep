package com.example.projekt_sklep.controller;

import com.example.projekt_sklep.model.Category;
import com.example.projekt_sklep.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public String showCategories(Model model) {

        model.addAttribute("categories", categoryRepository.findAll());

        return "categories";
    }

    @GetMapping("/categories/add")
    public String addCategoryForm(Model model) {

        model.addAttribute("category", new Category());

        return "addCategory";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@Valid Category category,
                               BindingResult result) {

        if(result.hasErrors()){
            return "addCategory";
        }

        categoryRepository.save(category);

        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id){

        categoryRepository.deleteById(id);

        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Long id,
                               Model model){

        Category category =
                categoryRepository.findById(id).orElse(null);

        model.addAttribute("category", category);

        return "editCategory";
    }

    @PostMapping("/categories/update")
    public String updateCategory(Category category){

        categoryRepository.save(category);

        return "redirect:/categories";
    }
}