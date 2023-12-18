package com.example.cafemanagementsystem.service;

import com.example.cafemanagementsystem.dto.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<String> singUp(Map<String, String> requestMap);

    ResponseEntity<String> login(Map<String, String> requestMap);

    ResponseEntity<List<UserDto>> getAllUser();

    ResponseEntity<String> update(Map<String, String> requestMap);
}
