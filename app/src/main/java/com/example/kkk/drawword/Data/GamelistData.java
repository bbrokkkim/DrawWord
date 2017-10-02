package com.example.kkk.drawword.Data;

/**
 * Created by KKK on 2017-08-17.
 */

public class GamelistData {
    int room_num;
    String room_name;
    String room_con;
    public GamelistData(int room_num, String room_name, String room_con){
        this.room_num = room_num;
        this.room_name = room_name;
        this.room_con = room_con;
    }

    public void setRoom_num(int room_num) {
        this.room_num = room_num;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public void setRoom_con(String room_con) {
        this.room_con = room_con;
    }

    public int getRoom_num() {
        return room_num;
    }

    public String getRoom_name() {
        return room_name;
    }

    public String getRoom_con() {
        return room_con;
    }
}
