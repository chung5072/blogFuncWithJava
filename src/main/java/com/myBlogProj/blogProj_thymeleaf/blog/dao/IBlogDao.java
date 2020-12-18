package com.myBlogProj.blogProj_thymeleaf.blog.dao;

import com.myBlogProj.blogProj_thymeleaf.blog.Background;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IBlogDao {
    int imageInsert(int backgroundID, String backgroundTitle,
                    MultipartFile backgroundImage) throws IOException;
    List<Background> findAllBackground();
    int setAsBackground(int backgroundCount);
    int releaseBackground(int backgroundCount);
    int resetBackground();
    String getBlogTitle();
    int changeBlogTitle(String newBlogTitle);
}
