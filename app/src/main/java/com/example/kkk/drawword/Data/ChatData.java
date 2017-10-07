package com.example.kkk.drawword.Data;

/**
 * Created by KKK on 2017-08-25.
 */

public class ChatData {
    String user_name;
    String ment;
    boolean check;

    public ChatData(String user_name,String ment,boolean check){
        this.ment = ment;
        this.user_name = user_name;
        this.check = check;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setMent(String ment) {
        this.ment = ment;
    }

    public boolean isCheck() {
        return check;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getMent() {
        return ment;
    }
}
