package com.example.cafemanagementsystem.entity;


import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


@NamedQuery(name = "Category.getAllCategory",query = "select  c from Category c")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Integer id;


    @Column(name = "name")
    private  String name;

}
