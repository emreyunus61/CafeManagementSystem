package com.example.cafemanagementsystem.service.Impl;

import com.example.cafemanagementsystem.repository.BillRepository;
import com.example.cafemanagementsystem.repository.CategoryRepository;
import com.example.cafemanagementsystem.repository.ProductRepository;
import com.example.cafemanagementsystem.service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboarServiceImpl implements DashboardService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private  final BillRepository billRepository;

    public DashboarServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, BillRepository billRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.billRepository = billRepository;
    }

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {

        Map<String,Object> map = new HashMap<>();
        map.put("category",categoryRepository.count());
        map.put("product",productRepository.count());
        map.put("bill",billRepository.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
