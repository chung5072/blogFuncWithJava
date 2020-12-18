package com.myBlogProj.blogProj_thymeleaf.blog;

import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;

public class Background {
    private int backgroundID;
    private String backgroundTitle;
    private byte[] backgroundImage;
    private String backgroundSetting;

    public int getBackgroundID() {
        return backgroundID;
    }

    public void setBackgroundID(int backgroundID) {
        this.backgroundID = backgroundID;
    }

    public String getBackgroundTitle() {
        return backgroundTitle;
    }

    public void setBackgroundTitle(String backgroundTitle) {
        this.backgroundTitle = backgroundTitle;
    }

    public byte[] getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(byte[] backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getBackgroundSetting() {
        return backgroundSetting;
    }

    public void setBackgroundSetting(String backgroundSetting) {
        this.backgroundSetting = backgroundSetting;
    }
}
