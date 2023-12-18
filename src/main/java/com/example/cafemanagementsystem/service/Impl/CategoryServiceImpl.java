package com.example.cafemanagementsystem.service.Impl;

import com.example.cafemanagementsystem.constents.CafeConstans;
import com.example.cafemanagementsystem.entity.Category;
import com.example.cafemanagementsystem.jwt.JwtFilter;
import com.example.cafemanagementsystem.repository.CategoryRepository;
import com.example.cafemanagementsystem.service.CategoryService;
import com.example.cafemanagementsystem.utils.CafeUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final JwtFilter jwtFilter;

    public CategoryServiceImpl(CategoryRepository categoryRepository, JwtFilter jwtFilter) {
        this.categoryRepository = categoryRepository;
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requsetMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateCategoryMap(requsetMap, false)) ;
                {
                    categoryRepository.save(getCategoryFromMap(requsetMap, false));
                    return CafeUtils.getResponseEntity("Category Added Succes", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstans.UNAUTHORIZED_ACCES, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstans.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requsetMap, boolean valideteId) {
        if (requsetMap.containsKey("name")) {
            if (requsetMap.containsKey("id") && valideteId) {
                return true;
            } else if (!valideteId) {
                return true;
            }
        }
        return false;
    }


    private Category getCategoryFromMap(Map<String, String> requsetMap, boolean isAdd) {
        Category category = new Category();
        if (isAdd) {
            category.setId(Integer.parseInt(requsetMap.get("id")));
        }
        category.setName(requsetMap.get("name"));
        return category;
    }


    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
                log.info("Inside if");
                return new ResponseEntity<List<Category>>(categoryRepository.getAllCategory(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requsetMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateCategoryMap(requsetMap, true)) {
                    Optional optional = categoryRepository.findById(Integer.parseInt(requsetMap.get("id")));
                    if (!optional.isEmpty()) {
                        categoryRepository.save(getCategoryFromMap(requsetMap, true));
                        return CafeUtils.getResponseEntity("Category Updated Succesfully", HttpStatus.OK);
                    } else {
                        return CafeUtils.getResponseEntity("Category id does not exist", HttpStatus.OK);
                    }
                }
                return CafeUtils.getResponseEntity(CafeConstans.INVALID_DATE, HttpStatus.BAD_REQUEST);
            } else {
                return CafeUtils.getResponseEntity(CafeConstans.UNAUTHORIZED_ACCES, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstans.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
