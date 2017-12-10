package com.example.kkk.drawword;

/**
 * Created by KKK on 2017-11-21.
 */

public class InvateList {
    String user_name;
    String room_num;
    boolean choice = true;
    public InvateList(String user_name,String room_num){
        this.user_name = user_name;
        this.room_num = room_num;

    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setRoom_num(String room_num) {
        this.room_num = room_num;
    }

    public void setChoice() {
        this.choice = false;
    }

    public boolean isChoice() {
        return choice;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getRoom_num() {
        return room_num;
    }
}
