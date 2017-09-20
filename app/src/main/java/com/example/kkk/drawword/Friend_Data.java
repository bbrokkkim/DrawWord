package com.example.kkk.drawword;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by KKK on 2017-08-17.
 */

public class Friend_Data {
    String user_name;
    String user_ment;
    String user_photo;
    String iden;
    public Friend_Data(String user_name, String user_ment,String user_photo,String iden){
//        this.user_photo = user_photo;
        this.user_name = user_name;
        this.user_ment = user_ment;
        this.user_photo = user_photo;
        this.iden = iden;
    }

/*    public void setUser_photo(Bitmap user_photo) {
        this.user_photo = user_photo;
    }*/

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
/*

    public Bitmap getUser_photo() {
        return user_photo;
    }
*/


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
}
