package com.myBlogProj.blogProj_thymeleaf.blog.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.myBlogProj.blogProj_thymeleaf.blog.Background;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BlogDao implements IBlogDao{

    private JdbcTemplate jdbcTemplate;

    public BlogDao(ComboPooledDataSource comboPooledDataSource) {
        this.jdbcTemplate = new JdbcTemplate(comboPooledDataSource);
    }

    @Override
    public int imageInsert(final int backgroundID, final String backgroundTitle,
                           final MultipartFile backgroundImage) throws IOException {
        byte[] backgroundImageBytes = backgroundImage.getBytes();

        int resultInsert = 0;

        final String backgroundInsert_sql =
                "INSERT INTO testbackground3(backgroundID, backgroundTitle, backgroundImage) values (?, ?, ?)";

        resultInsert = jdbcTemplate.update(backgroundInsert_sql,
                backgroundID, backgroundTitle, backgroundImageBytes);

        return resultInsert;
    }

    @Override
    public List<Background> findAllBackground() {
        /*카테고리의 전체 내용을 가져옴*/
        final String backgroundSelect_sql =
                "SELECT * FROM testbackground3";

        return jdbcTemplate.query(backgroundSelect_sql,
                new BeanPropertyRowMapper<>(Background.class));
    }

    @Override
    public int setAsBackground(int backgroundCount) {
        int resultUpdate = 0;

        final String sql = "UPDATE testbackground3 " +
                "SET backgroundSetting = ? " +
                "WHERE backgroundID = ?";

        resultUpdate = jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, "T");
                pstmt.setInt(2, backgroundCount);
            }
        });
        return resultUpdate;
    }

    @Override
    public int releaseBackground(int backgroundCount) {
        int resultUpdate = 0;

        final String sql = "UPDATE testbackground3 " +
                "SET backgroundSetting = ? " +
                "WHERE backgroundID = ?";

        resultUpdate = jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, "F");
                pstmt.setInt(2, backgroundCount);
            }
        });
        return resultUpdate;
    }

    @Override
    public int resetBackground() {
        int resultReset = 0;

        final String sql = "UPDATE testbackground3 " +
                "SET backgroundSetting = ? " +
                "WHERE backgroundSetting = ?";

        resultReset = jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, "F");
                pstmt.setString(2, "T");
            }
        });
        return resultReset;
    }

    @Override
    public String getBlogTitle() {
        int id = 1;
        final String blogTitleSelect_sql =
                "SELECT blogTitle FROM testblogtitle WHERE blogTitleID = ?";

        return jdbcTemplate.queryForObject(
                blogTitleSelect_sql, new Object[] { id }, String.class);
    }

    @Override
    public int changeBlogTitle(String newBlogTitle) {
        int resultUpdate = 0;

        final String sql = "UPDATE testblogtitle " +
                "SET blogTitle = ? " +
                "WHERE blogTitleID = ?";

        resultUpdate = jdbcTemplate.update(sql, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, newBlogTitle);
                pstmt.setInt(2, 1);
            }
        });
        return resultUpdate;
    }
}
