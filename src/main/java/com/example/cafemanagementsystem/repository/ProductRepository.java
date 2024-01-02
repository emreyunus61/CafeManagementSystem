package com.example.cafemanagementsystem.repository;

import com.example.cafemanagementsystem.dto.ProductDto;
import com.example.cafemanagementsystem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<ProductDto> getAllProduct();

    @Modifying
    @Transactional
    Integer  updateProductStatus(@Param("status") String status,@Param("id") Integer id);

    List<ProductDto> getProductByCategory(@Param("id") Integer id);

    ProductDto getProductById(@Param("id") Integer id);
}
