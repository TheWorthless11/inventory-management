package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.Users;
import com.example.inventorymanagement.service.CategoryService;
import com.example.inventorymanagement.service.ProductService;
import com.example.inventorymanagement.service.SupplierService;
import com.example.inventorymanagement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ui")
public class PageController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final UserService userService;

    public PageController(ProductService productService,
                          CategoryService categoryService,
                          SupplierService supplierService,
                          UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
        this.userService = userService;
    }

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        addNavbar(model);
        model.addAttribute("productCount", productService.getAllProducts().size());
        model.addAttribute("categoryCount", categoryService.getAllCategories().size());
        model.addAttribute("supplierCount", supplierService.getAllSuppliers().size());
        return "dashboard";
    }

    @GetMapping("/products")
    public String productsPage(Model model) {
        addNavbar(model);
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @GetMapping("/categories")
    public String categoriesPage(Model model) {
        addNavbar(model);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }

    @GetMapping("/suppliers")
    public String suppliersPage(Model model) {
        addNavbar(model);
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        return "suppliers";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        addNavbar(model);
        model.addAttribute("userForm", new Users());
        model.addAttribute("roles", List.of("ADMIN", "SELLER", "BUYER"));
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userForm") Users user,
                               RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User registered successfully.");
            return "redirect:/ui/register";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/ui/register";
        }
    }

    private void addNavbar(Model model) {
        Map<String, String> navLinks = new LinkedHashMap<>();
        navLinks.put("Dashboard", "/ui/dashboard");
        navLinks.put("Products", "/ui/products");
        navLinks.put("Categories", "/ui/categories");
        navLinks.put("Suppliers", "/ui/suppliers");
        navLinks.put("Register User", "/ui/register");
        model.addAttribute("navLinks", navLinks);
    }
}

