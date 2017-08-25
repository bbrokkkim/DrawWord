package com.example.kkk.drawword;

/**
 * Created by KKK on 2017-08-25.
 */

public class ChatData {
    String user_name;
    String ment;

    public ChatData(String user_name,String ment){
        this.ment = ment;
        this.user_name = user_name;

    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setMent(String ment) {
        this.ment = ment;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getMent() {
        return ment;
    }
}
