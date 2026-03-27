package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.Category;
import com.example.inventorymanagement.entity.Product;
import com.example.inventorymanagement.entity.Users;
import com.example.inventorymanagement.exception.ResourceNotFoundException;
import com.example.inventorymanagement.repository.CategoryRepository;
import com.example.inventorymanagement.repository.ProductRepository;
import com.example.inventorymanagement.repository.StockLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    private final StockLogService stockLogService;
    private final UserService userService;

    @Autowired
    public ProductService(ProductRepository productRepository,CategoryRepository categoryRepository,StockLogService stockLogService,UserService userService) {
        this.productRepository=productRepository;
        this.categoryRepository=categoryRepository;
        this.stockLogService=stockLogService;
        this.userService=userService;

    }

    //1.get all products
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    //2.create a product
    public Product createProduct(Product product,Long categoryId){
        // Rule A: Price cannot be negative
        if (product.getPrice()<0){
            throw new IllegalArgumentException("prices cannot be less than zero!");
        }

        // Rule B: Stock cannot be negative
        if(product.getStockQuantity()<0){
            throw new IllegalArgumentException("Stock quantity cannot be negative!");
        }

        // Rule C: The Category MUST exist in the database
        // REPLACED RuntimeException with ResourceNotFoundException
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot add product: Category ID " + categoryId + " does not exist!"));
// Attach the real category to the product
        product.setCategory(category);

        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Product updatedProduct, Long categoryId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product ID " + productId + " not found!"));

        if (updatedProduct.getName() == null || updatedProduct.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty!");
        }
        if (updatedProduct.getPrice() < 0) {
            throw new IllegalArgumentException("prices cannot be less than zero!");
        }
        if (updatedProduct.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative!");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category ID " + categoryId + " does not exist!"));

        existingProduct.setName(updatedProduct.getName().trim());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
        existingProduct.setCategory(category);

        return productRepository.save(existingProduct);
    }

// 3. DELETE A PRODUCT
    public void deleteProduct(Long productId){
        if(!productRepository.existsById(productId)){
            throw new ResourceNotFoundException("Cannot delete: Product ID " + productId + " not found!");
        }
        productRepository.deleteById(productId);
    }

    // 4. UPDATE STOCK QUANTITY (When someone buys or restocks an item)
    public Product updateStock(Long productId, int newQuantity,String username){
        Product existingProduct = productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not found!"));

        if (newQuantity<0){
            throw new IllegalArgumentException("Cannot update stock to a negative number.");
        }

        // --- THE STOCK LOGIC ---
        // 1. Figure out how much the stock is changing by
        int oldQuantity = existingProduct.getStockQuantity();
        int quantityDifference = newQuantity - oldQuantity;

        // 2. Determine the transaction type (If we added stock = RESTOCK, if we lost stock = SALE)
        String transactionType = (quantityDifference>0) ? "RESTOCK" : "SALE";

        // 3. Save the new quantity to the database
        existingProduct.setStockQuantity(newQuantity);
        Product savedProduct = productRepository.save(existingProduct);

        // 4. Fetch the User who made the change
        Users loggedInUser = userService.getUserByUsername(username);

        // 5.
        // We use Math.abs() so the log always shows a positive number (e.g., "Sold 5" instead of "Sold -5")
        stockLogService.recordLog(savedProduct,loggedInUser,Math.abs(quantityDifference),transactionType);


        return savedProduct;
    }
}
