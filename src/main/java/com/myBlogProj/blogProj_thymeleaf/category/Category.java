package com.myBlogProj.blogProj_thymeleaf.category;

public class Category {
    private String categoryTitle;
    private String categoryClosed;

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryClosed() {
        return categoryClosed;
    }

    public void setCategoryClosed(String categoryClosed) {
        this.categoryClosed = categoryClosed;
    }
}
