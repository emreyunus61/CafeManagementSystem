package com.example.cafemanagementsystem.repository;

import com.example.cafemanagementsystem.dto.ProductDto;
import com.example.cafemanagementsystem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<ProductDto> getAllProduct();
}
