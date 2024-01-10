package com.example.cafemanagementsystem.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name = "Bill.getAllBills",query = "select b from Bill b order by b.id desc ")

@NamedQuery(name = "Bill.getBillByUserName",query = "select b from Bill b where b.createdBy=:username order by b.id desc ")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "bill")
public class Bill implements Serializable {

    private  static  final  long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private  Integer id;


    @Column(name = "uuid")
    private String uuid;

    @Column(name = "name")
    private String name;


    @Column(name = "email")
    private String email;

    @Column(name = "contactnumber")
    private String contactNumber;

    @Column(name = "paymentmethod")
    private String paymentMethod;


    @Column(name = "total")
    private Integer total;

    @Column(name = "productdetails")
    private  String productDetail;

    @Column(name = "createdby")
    private  String createdBy;

    //  @Column(name = "productdetails",columnDefinition = "json")
    // "productdetails" sütununun "json" veri tipine sahip olduğunu belirtir

}
