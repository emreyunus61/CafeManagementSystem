package com.example.cafemanagementsystem.service.Impl;

import com.example.cafemanagementsystem.constents.CafeConstans;
import com.example.cafemanagementsystem.dto.ProductDto;
import com.example.cafemanagementsystem.entity.Category;
import com.example.cafemanagementsystem.entity.Product;
import com.example.cafemanagementsystem.jwt.JwtFilter;
import com.example.cafemanagementsystem.repository.ProductRepository;
import com.example.cafemanagementsystem.service.ProductService;
import com.example.cafemanagementsystem.utils.CafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final JwtFilter jwtFilter;

    public ProductServiceImpl(ProductRepository productRepository, JwtFilter jwtFilter) {
        this.productRepository = productRepository;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requsetMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requsetMap, false)) {
                    productRepository.save(getProductFromMap(requsetMap, false));
                    return CafeUtils.getResponseEntity("Product Added Succes", HttpStatus.OK);

                } else
                    return CafeUtils.getResponseEntity(CafeConstans.INVALID_DATE, HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstans.UNAUTHORIZED_ACCES, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstans.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validateProductMap(Map<String, String> requsetMap, boolean validateId) {
        if (requsetMap.containsKey("name")) {
            if (requsetMap.containsKey("id") && validateId) {
                return true;
            } else if (!validateId) {
                return true;
            }
        }
        return false;
    }


    private Product getProductFromMap(Map<String, String> requsetMap, boolean isAdd) {

        Category category = new Category();
        category.setId(Integer.parseInt(requsetMap.get("categoryId")));

        Product product = new Product();
        if (isAdd) {
            product.setId(Integer.parseInt(requsetMap.get("id")));
        } else {
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requsetMap.get("name"));
        product.setDescription(requsetMap.get("description"));
        product.setPrice(Integer.parseInt(requsetMap.get("price")));
        return product;
    }


    @Override
    public ResponseEntity<List<ProductDto>> getAllProduct() {
        try {
            return new ResponseEntity<>(productRepository.getAllProduct(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requsetMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requsetMap, true)) {
                    Optional<Product> optional = productRepository.findById(Integer.parseInt(requsetMap.get("id")));
                    if (!optional.isEmpty()) {
                        Product product = getProductFromMap(requsetMap, true);
                        product.setStatus(optional.get().getStatus());
                        productRepository.save(product);
                        return CafeUtils.getResponseEntity("Product Updated Succesfully", HttpStatus.OK);

                    } else {
                        return CafeUtils.getResponseEntity("Product id does not exist", HttpStatus.OK);
                    }

                } else {
                    return CafeUtils.getResponseEntity(CafeConstans.INVALID_DATE, HttpStatus.BAD_REQUEST);
                }

            } else {
                return CafeUtils.getResponseEntity(CafeConstans.UNAUTHORIZED_ACCES, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstans.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
