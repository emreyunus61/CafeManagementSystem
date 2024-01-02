package com.example.cafemanagementsystem.dto;

import lombok.Data;


@Data
public class ProductDto {

    Integer id;

    String name;

    String description;

    Integer price;

    String status;

    Integer categoryId;

    String categoryName;


    public ProductDto(){

    }

    public ProductDto(Integer id, String name, String description, Integer price, String status, Integer categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }


    public ProductDto(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public ProductDto(Integer id, String name, String description,Integer price){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

}
