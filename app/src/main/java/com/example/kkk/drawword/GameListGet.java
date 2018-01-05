package com.example.kkk.drawword;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by KKK on 2017-11-13.
 */

public class GameListGet {

    private static ArrayList<String> GameList = new ArrayList<>();
    private static int focus = 0;

    private static GameListGet uniquelnstance = new GameListGet();

    private GameListGet(){
    }
    public static GameListGet getInstance(){
        if (uniquelnstance == null){
            uniquelnstance = new GameListGet();
        }
        return uniquelnstance;
    }

    public void resetGameList(){
        GameList = new ArrayList<>();
    }
    public void resetFouce(){
        focus = 0;
    }

    public void addGameList(String row){
        GameList.add(row);
    }
    public String getGameList(int choice){
        return GameList.get(choice);
    }

    public int sizeGameList(){
        return GameList.size();
    }

    public static int getFocus() {
        return focus;
    }

    public static void setFocus(int focus) {
        GameListGet.focus = focus;
    }

    public static ArrayList<String> getGameList() {
        return GameList;
    }
}
