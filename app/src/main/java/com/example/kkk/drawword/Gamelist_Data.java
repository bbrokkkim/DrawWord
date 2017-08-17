package com.example.kkk.drawword;

/**
 * Created by KKK on 2017-08-17.
 */

public class Gamelist_Data {
    String room_num;
    String room_name;
    String room_con;
    public Gamelist_Data(String room_num,String room_name,String room_con){
        this.room_num = room_num;
        this.room_name = room_name;
        this.room_con = room_con;
    }

    public void setRoom_num(String room_num) {
        this.room_num = room_num;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public void setRoom_con(String room_con) {
        this.room_con = room_con;
    }

    public String getRoom_num() {
        return room_num;
    }

    public String getRoom_name() {
        return room_name;
    }

    public String getRoom_con() {
        return room_con;
    }
}
