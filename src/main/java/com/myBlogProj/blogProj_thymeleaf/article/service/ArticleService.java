package com.myBlogProj.blogProj_thymeleaf.article.service;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.myBlogProj.blogProj_thymeleaf.article.Article;
import com.myBlogProj.blogProj_thymeleaf.article.dao.ArticleDao;
import com.myBlogProj.blogProj_thymeleaf.category.Category;
import com.myBlogProj.blogProj_thymeleaf.test.FileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class ArticleService implements IArticleService{

    private JdbcTemplate jdbcTemplate;

    public ArticleService(ComboPooledDataSource comboPooledDataSource) {
        this.jdbcTemplate = new JdbcTemplate(comboPooledDataSource);
    }

    @Autowired
    ArticleDao articleDao;

    @Override
    public void articleRegister(Article newArticle) throws IOException {
        int resultRegister = articleDao.articleInsert(newArticle);

        if (resultRegister == 0){
            System.out.println("Article Register Failed!");
        }
        else{
            System.out.println("Article Register Success!");
        }
    }

    @Override
    public List<Article> findAllArticle() {
        /*카테고리의 전체 내용을 가져옴*/
        final String articleSelect_sql =
                "SELECT * FROM testarticle20 ORDER BY articleCategory ASC";

        /*jdbctemplate + BeanProepertyRowMapper를 활용하여 Category의 내용을 전부 가져옴
         * 대신에 Category 클래스를 생성해 놓아야.*/
        return jdbcTemplate.query(articleSelect_sql,
                new BeanPropertyRowMapper<>(Article.class));
    }
}
