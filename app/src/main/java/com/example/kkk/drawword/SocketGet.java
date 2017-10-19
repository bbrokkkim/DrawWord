package com.example.kkk.drawword;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Serializable;
import java.net.Socket;

/**
 * Created by KKK on 2017-10-14.
 */

public class SocketGet implements Serializable{

    Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    public  SocketGet(Socket socket, BufferedReader bufferedReader ,BufferedWriter bufferedWriter){
        this.socket = socket;
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public void setBufferedWriter(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
    }


    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

}
