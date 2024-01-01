package com.example.cafemanagementsystem.service;

import com.example.cafemanagementsystem.dto.ProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ResponseEntity<String> addNewProduct(Map<String, String> requsetMap);

    ResponseEntity<List<ProductDto>> getAllProduct();

    ResponseEntity<String> updateProduct(Map<String, String> requsetMap);
}
