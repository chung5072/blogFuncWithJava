package com.myBlogProj.blogProj_thymeleaf.category.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.myBlogProj.blogProj_thymeleaf.category.Category;
import com.myBlogProj.blogProj_thymeleaf.member.Member;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Repository
public class CategoryDao implements ICategoryDao{

    private JdbcTemplate jdbcTemplate;

    public CategoryDao(ComboPooledDataSource comboPooledDataSource) {
        this.jdbcTemplate = new JdbcTemplate(comboPooledDataSource);
    }

    @Override
    public int categoryInsert(final Category category) {

        int resultInsert = 0;

        final String categoryInsert_sql =
                "INSERT INTO testcategory20 (categoryTitle, categoryClosed) values (?, ?)";

        resultInsert = jdbcTemplate.update(categoryInsert_sql,
                category.getCategoryTitle(), category.getCategoryClosed());

//        jdbcTemplate.update(categoryInsert_sql, category.getCategoryTitle(), category.getCategoryId());

        return resultInsert;
    }

    @Override
    public int categoryChange(String previousCategoryName, String afterCategoryName, String categoryClosedChange) {
        int resultUpdate = 0;

        if (afterCategoryName.isEmpty()) {
            final String sqlCategoryClosedChange = "UPDATE testcategory20 SET categoryClosed = ? WHERE categoryTitle = ?";

            resultUpdate = jdbcTemplate.update(sqlCategoryClosedChange, new PreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement pstmt) throws SQLException {
                    pstmt.setString(1, categoryClosedChange);
                    pstmt.setString(2, previousCategoryName);
                }
            });

        } else if (categoryClosedChange.isEmpty()) {
            final String sqlCategoryNameChange = "UPDATE testcategory20 SET categoryTitle = ? WHERE categoryTitle = ?";

            resultUpdate = jdbcTemplate.update(sqlCategoryNameChange, new PreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement pstmt) throws SQLException {
                    pstmt.setString(1, afterCategoryName);
                    pstmt.setString(2, previousCategoryName);
                }
            });

        } else {
            final String sqlAllChange = "UPDATE testcategory20 SET categoryTitle = ?, categoryClosed = ? WHERE categoryTitle = ?";

            resultUpdate = jdbcTemplate.update(sqlAllChange, new PreparedStatementSetter() {

                @Override
                public void setValues(PreparedStatement pstmt) throws SQLException {
                    pstmt.setString(1, afterCategoryName);
                    pstmt.setString(2, categoryClosedChange);
                    pstmt.setString(3, previousCategoryName);
                }
            });
        }


        return resultUpdate;
    }

    @Override
    public int categoryDelete(String categoryDelete) {
        int resultUpdate = 0;

        final String sql = "DELETE FROM testcategory20 WHERE categoryTitle = ?";

        resultUpdate = jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, categoryDelete);
            }
        });
        return resultUpdate;
    }

}
