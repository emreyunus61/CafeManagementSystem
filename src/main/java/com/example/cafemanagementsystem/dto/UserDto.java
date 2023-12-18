package com.example.cafemanagementsystem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor //Parametresiz consturctor oluşmasını sağlar
public class UserDto {

    private  Integer id;
    private  String name;
    private  String email;
    private  String contactNumber;
    private  String status;

    public UserDto(Integer id, String name, String email, String contactNumber, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.status = status;
    }
}
