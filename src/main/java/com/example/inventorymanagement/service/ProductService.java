package com.example.inventorymanagement.service;

import com.example.inventorymanagement.entity.Category;
import com.example.inventorymanagement.entity.Product;
import com.example.inventorymanagement.repository.CategoryRepository;
import com.example.inventorymanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,CategoryRepository categoryRepository){
        this.productRepository=productRepository;
        this.categoryRepository=categoryRepository;

    }

    //1.get all products
    public List<Product> getAllProduct(){
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
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new RuntimeException("Cannot add product: Category ID " + categoryId + "does not exist!"));
// Attach the real category to the product
        product.setCategory(category);

        return productRepository.save(product);
    }

// 3. DELETE A PRODUCT
    public void deleteProduct(Long productId){
        if(!productRepository.existsById(productId)){
            throw new RuntimeException("Cannot delete: Product ID " + productId + " not found!");
        }
        productRepository.deleteById(productId);
    }

    // 4. UPDATE STOCK QUANTITY (When someone buys or restocks an item)
    public Product updateStock(Long productId, int newQuantity){
        Product existingProduct = productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product not found!"));

        if (newQuantity<0){
            throw new IllegalArgumentException("Cannot update stock to a negative number.");
        }

        existingProduct.setStockQuantity(newQuantity);
        Product savedProduct = productRepository.save(existingProduct);

        // TODO: Call  teammate's StockLogService right here to record who changed the stock!

        return savedProduct;
    }
}
