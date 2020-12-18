package com.myBlogProj.blogProj_thymeleaf.category.service;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.myBlogProj.blogProj_thymeleaf.category.Category;
import com.myBlogProj.blogProj_thymeleaf.category.dao.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CategoryService implements ICategoryService{

    private JdbcTemplate jdbcTemplate;

    public CategoryService(ComboPooledDataSource comboPooledDataSource) {
        this.jdbcTemplate = new JdbcTemplate(comboPooledDataSource);
    }

    @Autowired
    CategoryDao categoryDao;

    @Override
    public void categoryRegister(Category category) {
        int resultRegister = categoryDao.categoryInsert(category);

        if (resultRegister == 0){
            System.out.println("Category Register Failed!");
        }
        else{
            System.out.println("Category Register Success!");
        }
    }

    @Override
    public List<Category> findAll() {
        /*카테고리의 전체 내용을 가져옴*/
        final String categorySelect_sql =
                "SELECT * FROM testcategory20 ORDER BY categoryNum ASC";

        /*jdbctemplate + BeanProepertyRowMapper를 활용하여 Category의 내용을 전부 가져옴
        * 대신에 Category 클래스를 생성해 놓아야.*/
        return jdbcTemplate.query(categorySelect_sql,
                new BeanPropertyRowMapper<>(Category.class));
    }
}
