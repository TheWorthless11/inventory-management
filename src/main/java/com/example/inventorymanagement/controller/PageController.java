package com.example.inventorymanagement.controller;

import com.example.inventorymanagement.entity.Category;
import com.example.inventorymanagement.entity.Product;
import com.example.inventorymanagement.entity.Supplier;
import com.example.inventorymanagement.entity.Users;
import com.example.inventorymanagement.service.CategoryService;
import com.example.inventorymanagement.service.ProductService;
import com.example.inventorymanagement.service.StockLogService;
import com.example.inventorymanagement.service.SupplierService;
import com.example.inventorymanagement.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ui")
public class PageController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final UserService userService;
    private final StockLogService stockLogService;

    public PageController(ProductService productService,
                          CategoryService categoryService,
                          SupplierService supplierService,
                          UserService userService,
                          StockLogService stockLogService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
        this.userService = userService;
        this.stockLogService = stockLogService;
    }

    @GetMapping({"", "/", "/dashboard"})
    @PreAuthorize("hasAnyRole('ADMIN','SELLER','BUYER')")
    public String dashboard(Model model) {
        model.addAttribute("productCount", productService.getAllProducts().size());
        model.addAttribute("categoryCount", categoryService.getAllCategories().size());
        model.addAttribute("supplierCount", supplierService.getAllSuppliers().size());
        return "dashboard";
    }

    @GetMapping("/products")
    @PreAuthorize("hasAnyRole('ADMIN','SELLER','BUYER')")
    public String productsPage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("productForm", new Product());
        return "products";
    }

    @PostMapping("/products")
    @PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    public String createProduct(@ModelAttribute("productForm") Product product,
                                @RequestParam Long categoryId,
                                RedirectAttributes redirectAttributes) {
        try {
            productService.createProduct(product, categoryId);
            redirectAttributes.addFlashAttribute("successMessage", "Product created successfully.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/ui/products";
    }

    @PostMapping("/products/{id}/update")
    @PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute Product product,
                                @RequestParam Long categoryId,
                                RedirectAttributes redirectAttributes) {
        try {
            productService.updateProduct(id, product, categoryId);
            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/ui/products";
    }

    @PostMapping("/products/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    public String updateStock(@PathVariable Long id,
                              @RequestParam int newQuantity,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        try {
            productService.updateStock(id, newQuantity, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Stock updated successfully.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/ui/products";
    }

    @PostMapping("/products/{id}/delete")
    @PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/ui/products";
    }

    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('ADMIN','SELLER','BUYER')")
    public String categoriesPage(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("categoryForm", new Category());
        return "categories";
    }

    @PostMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public String createCategory(@ModelAttribute("categoryForm") Category category,
                                 RedirectAttributes redirectAttributes) {
        try {
            categoryService.createCategory(category);
            redirectAttributes.addFlashAttribute("successMessage", "Category created successfully.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/ui/categories";
    }

    @PostMapping("/categories/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateCategory(@PathVariable Long id,
                                 @ModelAttribute Category category,
                                 RedirectAttributes redirectAttributes) {
        try {
            categoryService.updateCategory(id, category);
            redirectAttributes.addFlashAttribute("successMessage", "Category updated successfully.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/ui/categories";
    }

    @PostMapping("/categories/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/ui/categories";
    }

    @GetMapping("/suppliers")
    @PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    public String suppliersPage(Model model) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("supplierForm", new Supplier());
        return "suppliers";
    }

    @PostMapping("/suppliers")
    @PreAuthorize("hasRole('ADMIN')")
    public String createSupplier(@ModelAttribute("supplierForm") Supplier supplier,
                                 RedirectAttributes redirectAttributes) {
        try {
            supplierService.addSupplier(supplier);
            redirectAttributes.addFlashAttribute("successMessage", "Supplier created successfully.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/ui/suppliers";
    }

    @PostMapping("/suppliers/{id}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateSupplier(@PathVariable Long id,
                                 @ModelAttribute Supplier supplier,
                                 RedirectAttributes redirectAttributes) {
        try {
            supplierService.updateSupplier(id, supplier);
            redirectAttributes.addFlashAttribute("successMessage", "Supplier updated successfully.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/ui/suppliers";
    }

    @PostMapping("/suppliers/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteSupplier(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            supplierService.deleteSupplier(id);
            redirectAttributes.addFlashAttribute("successMessage", "Supplier deleted successfully.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/ui/suppliers";
    }

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public String logsPage(Model model) {
        model.addAttribute("logs", stockLogService.getAllLogs());
        return "logs";
    }

    @GetMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public String registerPage(Model model) {
        model.addAttribute("userForm", new Users());
        model.addAttribute("roles", List.of("ADMIN", "SELLER", "BUYER"));
        return "register";
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
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
}

