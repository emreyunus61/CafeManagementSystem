package com.example.cafemanagementsystem.controller;

import com.example.cafemanagementsystem.constents.CafeConstans;
import com.example.cafemanagementsystem.service.UserService;
import com.example.cafemanagementsystem.utils.CafeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
