package com.example.cafemanagementsystem.controller;

import com.example.cafemanagementsystem.constents.CafeConstans;
import com.example.cafemanagementsystem.dto.UserDto;
import com.example.cafemanagementsystem.service.UserService;
import com.example.cafemanagementsystem.utils.CafeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private  final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<String> singUp(@RequestBody(required = true)Map<String,String> requestMap){
        try {
            return  userService.singUp(requestMap);

        }catch (Exception e) {
            e.printStackTrace();
        }

        return  CafeUtils.getResponseEntity(CafeConstans.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody(required = true)Map<String,String> requestMap){
        try {
            return  userService.login(requestMap);

        }catch (Exception e) {
            e.printStackTrace();
        }

        return  CafeUtils.getResponseEntity(CafeConstans.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping(path = "/get")
    public  ResponseEntity<List<UserDto>>  getAllUser() {
        try {
         return userService.getAllUser();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new  ResponseEntity<List<UserDto>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody(required = true)Map<String,String> requestMap){
        try {
            return  userService.update(requestMap);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return  CafeUtils.getResponseEntity(CafeConstans.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
