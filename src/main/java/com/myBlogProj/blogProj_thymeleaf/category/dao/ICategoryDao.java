package com.myBlogProj.blogProj_thymeleaf.category.dao;

import com.myBlogProj.blogProj_thymeleaf.category.Category;

public interface ICategoryDao {
    int categoryInsert(Category category);
    int categoryChange(String previousCategoryName, String afterCategoryName, String categoryClosedChange);
    int categoryDelete(String categoryDelete);
}
