package com.example.kkk.drawword.Data;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by KKK on 2017-08-17.
 */

public class FriendData {
    String user_name;
    String user_ment;
    String user_photo;
    String iden;
    String rotate;
    Boolean choice;
    public FriendData(String user_name, String user_ment, String user_photo, String iden, String rotate){
        this.user_name = user_name;
        this.user_ment = user_ment;
        this.user_photo = user_photo;
        this.iden = iden;
        this.choice = false;
        this.rotate = rotate;
    }

    public void setIden(String iden) {
        this.iden = iden;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_ment(String user_ment) {
        this.user_ment = user_ment;
    }

    public void setRotate(String rotate) {
        this.rotate = rotate;
    }

    public void setChoice() {
        if (this.choice){
            this.choice = false;
        }
        else
            this.choice = true;
    }

    public Boolean getChoice() {
        return choice;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_ment() {
        return user_ment;
    }

    public String getIden() {
        return iden;
    }

    public String getRotate() {
        return rotate;
    }
}

