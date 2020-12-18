package com.myBlogProj.blogProj_thymeleaf.article.service;

import com.myBlogProj.blogProj_thymeleaf.article.Article;
import com.myBlogProj.blogProj_thymeleaf.test.FileUpload;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public interface IArticleService {
    void articleRegister(Article newArticle) throws IOException;
    List<Article> findAllArticle();
}
