package com.myBlogProj.blogProj_thymeleaf.category.service;

import com.myBlogProj.blogProj_thymeleaf.category.Category;

import java.util.List;

public interface ICategoryService {
    void categoryRegister(Category category);
    List<Category> findAll();
}
