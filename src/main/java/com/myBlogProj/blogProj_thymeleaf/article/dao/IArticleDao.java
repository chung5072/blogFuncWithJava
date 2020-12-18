package com.myBlogProj.blogProj_thymeleaf.article.dao;

import com.myBlogProj.blogProj_thymeleaf.article.Article;
import com.myBlogProj.blogProj_thymeleaf.article.Comment;
import com.myBlogProj.blogProj_thymeleaf.test.FileUpload;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public interface IArticleDao {
    List<Article> findAllArticle();
    int articleInsert(Article newArticle) throws IOException;
    int articleModify(String currentTitle, Article modifyArticle);
    int articleDelete(String currentTitle);
    int articleFileDelete(String articleTitle);

    List<Comment> findAllComment();
    int commentInsert(Comment comment, HttpSession httpSession, String commentTitle);
    int commentModify(String currentComment, String newComment);
    int commentDelete(String currentComment);
}
