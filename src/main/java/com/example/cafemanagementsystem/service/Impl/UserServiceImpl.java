package com.example.cafemanagementsystem.service.Impl;

import com.example.cafemanagementsystem.constents.CafeConstans;
import com.example.cafemanagementsystem.entity.User;
import com.example.cafemanagementsystem.repository.UserRepository;
import com.example.cafemanagementsystem.service.UserService;
import com.example.cafemanagementsystem.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private  final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public ResponseEntity<String> singUp(Map<String, String> requestMap) {
       log.info("Inside signUp {}" ,requestMap);

       try {
       if (validateSignUpMap(requestMap)){
           User user =userRepository.findByEmailId(requestMap.get("email"));
           if (Objects.isNull(user)){
            userRepository.save(getUserFromMap(requestMap));
            return  CafeUtils.getResponseEntity("Succesfuly Register.",HttpStatus.OK);
           }else {
            return  CafeUtils.getResponseEntity("Email already exist",HttpStatus.BAD_REQUEST);
           }
       }else {
           return CafeUtils.getResponseEntity(CafeConstans.INVALID_DATE, HttpStatus.BAD_REQUEST);
       }
       }catch (Exception e){
           e.printStackTrace();
       }
         return  CafeUtils.getResponseEntity(CafeConstans.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private  boolean validateSignUpMap(Map<String,String> requestMap) {
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return  true;
        }
        return  false;
    }


    // Örneğin, "name" anahtarına karşılık gelen değeri al ve User nesnesine ata
    //  user.setName(requestMap.get("name"));
    private  User getUserFromMap(Map<String,String> requestMap ) {

        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus(requestMap.get("false"));
        user.setRole(requestMap.get("user"));

        return  user;
    }

}
