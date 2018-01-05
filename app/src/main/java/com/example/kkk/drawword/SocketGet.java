package com.example.kkk.drawword;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by KKK on 2017-10-14.
 */

public class SocketGet{

    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;
    int a;


    private static SocketGet uniquelnstance = new SocketGet(socket,bufferedReader,bufferedWriter,2);

    private SocketGet(Socket socket, BufferedReader bufferedReader ,BufferedWriter bufferedWriter, int a){
        this.socket = socket;
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;
        this.a = a;
    }
    public static SocketGet getInstance(){
        if (uniquelnstance == null){
            uniquelnstance = new SocketGet(socket,bufferedReader,bufferedWriter,2);
        }
        return uniquelnstance;
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

    public void disconnectSocket(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void disconnectWirter(){
        try {
            this.bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void disconnectReader(){
        try {
            this.bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getA() {
        return a+2;
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