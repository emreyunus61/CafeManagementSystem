package com.example.cafemanagementsystem.service;

import com.example.cafemanagementsystem.entity.Bill;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BillService {
    ResponseEntity<String> generateReport(Map<String, Object> requsetMap);

    ResponseEntity<List<Bill>> getBills();

    ResponseEntity<byte[]> getPdf(Map<String, Object> requsetMap);

    ResponseEntity<String> deleteBill(Integer id);
}
