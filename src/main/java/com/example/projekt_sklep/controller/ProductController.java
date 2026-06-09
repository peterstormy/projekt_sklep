package com.example.projekt_sklep.controller;

import com.example.projekt_sklep.model.Product;
import com.example.projekt_sklep.repository.CategoryRepository;
import com.example.projekt_sklep.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/products")
    public String showProducts(Model model,
                               String sortField,
                               String sortDir,
                               Long categoryId,
                               String data,
                               HttpSession session) {

        if (sortField == null) {
            sortField = (String) session.getAttribute("sortField");
        }

        if (sortDir == null) {
            sortDir = (String) session.getAttribute("sortDir");
        }

        if (sortField == null) {
            sortField = "id";
        }

        if (sortDir == null) {
            sortDir = "asc";
        }

        session.setAttribute("sortField", sortField);
        session.setAttribute("sortDir", sortDir);

        Sort sort = sortDir.equals("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        if (data != null && !data.isEmpty()) {

            model.addAttribute(
                    "products",
                    productRepository.findByDataDodaniaGreaterThanEqual(
                            LocalDate.parse(data)
                    )
            );

        } else if (categoryId == null) {

            model.addAttribute(
                    "products",
                    productRepository.findAll(sort)
            );

        } else {

            model.addAttribute(
                    "products",
                    productRepository.findByCategoryId(
                            categoryId
                    )
            );

        }

        model.addAttribute(
                "categories",
                categoryRepository.findAll()
        );

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("data", data);

        return "products";
    }

    @GetMapping("/products/add")
    public String addProductForm(Model model) {

        model.addAttribute(
                "product",
                new Product()
        );

        model.addAttribute(
                "categories",
                categoryRepository.findAll()
        );

        return "addProduct";
    }

    @PostMapping("/products/save")
    public String saveProduct(@Valid Product product,
                              BindingResult result) {

        if (result.hasErrors()) {
            return "addProduct";
        }

        product.setDataDodania(
                LocalDate.now()
        );

        productRepository.save(product);

        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(
            @PathVariable Long id,
            Model model) {

        Product product =
                productRepository
                        .findById(id)
                        .orElse(null);

        model.addAttribute(
                "product",
                product
        );

        model.addAttribute(
                "categories",
                categoryRepository.findAll()
        );

        return "editProduct";
    }

    @PostMapping("/products/update")
    public String updateProduct(
            Product product) {

        productRepository.save(product);

        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id) {

        productRepository.deleteById(id);

        return "redirect:/products";
    }

    @GetMapping("/products/{id}")
    public String productDetail(
            @PathVariable Long id,
            Model model) {

        Product product =
                productRepository
                        .findById(id)
                        .orElse(null);

        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute("product", product);
        return "productDetail";
    }

    @GetMapping("/public/product/{id}")
    public String publicProduct(
            @PathVariable Long id,
            Model model){

        Product product =
                productRepository
                        .findById(id)
                        .orElse(null);

        if(product == null){
            return "redirect:/";
        }

        if(!product.isPubliczny()){
            return "redirect:/login";
        }

        model.addAttribute(
                "product",
                product
        );

        return "publicProduct";
    }
}