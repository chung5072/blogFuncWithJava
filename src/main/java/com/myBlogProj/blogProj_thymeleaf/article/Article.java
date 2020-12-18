package com.myBlogProj.blogProj_thymeleaf.article;

import org.springframework.web.multipart.MultipartFile;

public class Article {
    private String articleTitle;
    private String articleAuthor;
    private String articleDate;
    private String articleCategory;
    private String articleContents;
    private String articleFileName;
    private byte[] articleFileContents;

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public String getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(String articleDate) {
        this.articleDate = articleDate;
    }

    public String getArticleCategory() {
        return articleCategory;
    }

    public void setArticleCategory(String articleCategory) {
        this.articleCategory = articleCategory;
    }

    public String getArticleContents() {
        return articleContents;
    }

    public void setArticleContents(String articleContents) {
        this.articleContents = articleContents;
    }

    public String getArticleFileName() {
        return articleFileName;
    }

    public void setArticleFileName(String articleFileName) {
        this.articleFileName = articleFileName;
    }

    public byte[] getArticleFileContents() {
        return articleFileContents;
    }

    public void setArticleFileContents(byte[] articleFileContents) {
        this.articleFileContents = articleFileContents;
    }
}
