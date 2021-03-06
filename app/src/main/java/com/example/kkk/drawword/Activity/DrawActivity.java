package com.example.kkk.drawword.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.agsw.FabricView.FabricView;
import com.example.kkk.drawword.Adapter.DrawAdapter;
import com.example.kkk.drawword.Data.DrawData;
import com.example.kkk.drawword.Dialog;
import com.example.kkk.drawword.IntentClass;
import com.example.kkk.drawword.Okhttp.Tcp_connect;
import com.example.kkk.drawword.R;
import com.example.kkk.drawword.SocketGet;
import com.example.kkk.drawword.Okhttp.Tcp_chat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-10-13.
 */

public class DrawActivity extends Activity implements View.OnTouchListener{
    @BindView(R.id.draw_listview) ListView listView;
    @BindView(R.id.game_submit) Button submit;
    @BindView(R.id.answer_content) EditText answer_ed;
    @BindView(R.id.answer) TextView answer_view;
    @BindView(R.id.timer) TextView timer_view;
    @BindView(R.id.room_name) TextView room_name;
    @BindView(R.id.blind) Button blind;
    @BindView(R.id.faricView) FabricView fabricView;
    @BindView(R.id.choice_color) Spinner choice_color;
    @BindView(R.id.choice_thick) Spinner choice_thick;
    @BindView(R.id.modify) LinearLayout modify;
    @BindView(R.id.linear) LinearLayout linear;
    @BindView(R.id.insert_content) LinearLayout insert;
    @BindView(R.id.draworerase) ImageButton draw_or_erase;
    DrawAdapter drawAdapter;
    ArrayList<DrawData> item;
    ArrayList<String> result;
    //tcp
    BufferedWriter bufferedWriter;
    BufferedReader bufferedReader;
    Socket socket;

    Boolean draw_or_erase_boolean = true;
    float x,y,a,b,c,d;
    String content, to;
    String id = null;
    int idx;
    String start_draw = "";
    String finish_mant = "";
    String middle_draw = "";
    String end_draw = "";
    String room_num = "";
    String user_name = "";
    String tcp_type = "";
    String thick = "";
    String draw_color = "";
    String color = "";
    String x1 = "";
    String y1 = "";
    String a1 = "";
    String b1 = "";
    String answer = "";
    String cassandra_answer = "";
    String time = "";
    String draw_erase = "1";
    String status = "";
    String tagger_or_not = "2";
    String my_status = "challenger";
    SocketGet socketGet = SocketGet.getInstance();
    boolean draw_type;
    boolean exit = false;
    boolean connect_check_thread = true;
    MotionEvent event;
    IntentClass intentClass = new IntentClass(this);
    Tcp_chat tcp_chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_layout);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        room_num = intent.getStringExtra("room_num");
        status = intent.getStringExtra("status");

        room_name.setText(room_num + " 번방");
//        Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
        item = new ArrayList<>();
        draw_or_erase.setImageResource(R.mipmap.ic_launcher_erase);
        color = "BLACK";
        draw_color = "BLACK";
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        drawAdapter = new DrawAdapter(getLayoutInflater(),item);
        listView.setAdapter(drawAdapter);
        timer_view.bringToFront();
        modify.bringToFront();
        socket = socketGet.getSocket();
        bufferedReader = socketGet.getBufferedReader();
        bufferedWriter = socketGet.getBufferedWriter();
        checkUpdate.start();
//        checkConnectSocket.start();
        setSpinner();
        if (status.equals("1")) {
            blind.setVisibility(View.GONE);
//            Dialog("술래입니다.","준비되셧나요?",1);
        }
        else if (status.equals("2")){
            blind.setVisibility(View.VISIBLE); //비지블
        }

        fabricView.setBackgroundColor(Color.rgb(235,236,237));
//        fabricView = new FabricView(this,);
//        fabricView.setBackground();
        fabricView.setOnTouchListener(this);
        linear.setOnTouchListener(this);
        fabricView.cleanPage();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = answer_ed.getText().toString();
                if (!answer.equals("")) {
                    String push_content = "7《" + room_num + "《" + id + "《" + answer;
                    new Tcp_chat().execute(id, push_content);
                    drawAdapter.notifyDataSetChanged();
                    answer_ed.setText("");
                }
                FabricSetColor(color);
                FabricSetThick(thick);
            }
        });

        draw_or_erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DrawActivity.this, "지우기", Toast.LENGTH_SHORT).show();
                if (draw_or_erase_boolean == true){
                    draw_erase = "2";
                    draw_or_erase.setImageResource(R.mipmap.ic_launcher_write);
                    fabricView.setColor(Color.rgb(235,236,237));
                    draw_or_erase_boolean = false;

//                    socketGet.disconnectSocket(); //테스트를 위하여
//                    Toast.makeText(DrawActivity.this, "지우기", Toast.LENGTH_SHORT).show();
                }
                else {
                    draw_erase = "1";
                    draw_or_erase.setImageResource(R.mipmap.ic_launcher_erase);
                    FabricSetColor(color);
                    draw_or_erase_boolean = true;

                }
            }
        });
        choice_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                color = (String) choice_color.getSelectedItem();
                draw_color = (String) choice_color.getSelectedItem();
                if (!draw_erase.equals("2")) {
                    FabricSetColor(draw_color);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        choice_thick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                thick = (String) choice_thick.getSelectedItem();
                FabricSetThick(thick);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
/*        event = MotionEvent.obtain(7082122, 7082122, MotionEvent.ACTION_MOVE, x, y, 0);
        fabricView.onTouchDrawMode(event);*/
    }

    private void setSpinner() {
        SpinnerAdapter spinnerdapter;
        spinnerdapter = ArrayAdapter.createFromResource(this, R.array.draw_color,R.layout.spinner_text);
//        spinnerdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choice_color.setAdapter(spinnerdapter);
        choice_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,View view, int pos, long id) {
                String spinnerName = parent.getItemAtPosition(pos).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        SpinnerAdapter spinnerdapter2;
        spinnerdapter2 = ArrayAdapter.createFromResource(this, R.array.draw_thick,R.layout.spinner_text);
        choice_thick.setAdapter(spinnerdapter2);

    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "게임이 시작되어 나갈수 없습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this, "destroy", Toast.LENGTH_SHORT).show();
//        new Tcp_chat().execute(id ,"10《" + room_num + "《" + id + "》");
        exit = true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        a = fabricView.getWidth();
        b = fabricView.getHeight();
//        Toast.makeText(this, "" + a + " : " + b, Toast.LENGTH_SHORT).show();
        Log.d("xy","" + a + " : " + b);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    private Thread checkUpdate = new Thread() {
        public void run() {
            String line = null;
            Log.w("ChattingStart", "Start Thread");

            content = null;
            boolean test = false;
            while (true) {
//                Log.d("check", "start22222222222");
                boolean result = socketGet.getSocket().isConnected() && ! socketGet.getSocket().isClosed();
//                boolean connected = socket.isConnected() && ! socket.isClosed();
//                Log.d("stream", "서버 연결 확인 쓰레드 시작2");
                if (result) {
//                    Log.d("stream", "server connect complete~~~~~22222222222");

                    try {
                        while ((line = socketGet.getBufferedReader().readLine()) != null) {
                            Log.w("Chatting is running", "1");
                            Log.d("line2", line);

                            if (!line.contains("《")) {
                                Log.w("Chatting is error2", "error");
                                continue;
                            }

                            int idx_ment = line.indexOf("《");
                            String real_ment = line.substring(idx_ment + 1);
                            Log.d("made_line", real_ment);
                            int idx = real_ment.indexOf("《");
                            Log.d("chatting Test", String.valueOf(idx));
                            tcp_type = real_ment.substring(0, idx);
                            content = real_ment.substring(idx + 1);
                            Log.d("Chatting is content", content);
                            Log.d("idx_1___", String.valueOf(line));
                            //그리기
                            if (tcp_type.equals("3")) {
                                start_draw = content;
                                Log.d("Chatting is tcp_type", tcp_type);
                                Log.d("bb", String.valueOf(draw_type));
                                drawing.sendEmptyMessage(0);
                            }
                            //그리기
                            else if (tcp_type.equals("4")) {
                                middle_draw = content;
                                Log.d("Chatting is tcp_type", tcp_type);
                                Log.d("bb", String.valueOf(draw_type));
                                drawing.sendEmptyMessage(0);
                            }
                            //그리기
                            else if (tcp_type.equals("5")) {
                                end_draw = content;
                                Log.d("Chatting is tcp_type", tcp_type);
                                Log.d("bb", String.valueOf(draw_type));
                                drawing.sendEmptyMessage(0);
                            }
                            //술래
                            else if (tcp_type.equals("6")) {
                                //유저네임
                                my_status = "tagger";
                                idx = content.indexOf("《");
                                answer = content.substring(idx + 1);
                                user_name = content.substring(0, idx);
                                //답
                                idx = answer.indexOf("《");
                                answer = answer.substring(0, idx);
                                drawing_form.sendEmptyMessage(0);
                                Log.d("Chatting is tcp_type", tcp_type);
                            }
                            //도전자
                            else if (tcp_type.equals("6.1")) {
                                my_status = "challenger";
                                try_get_answer.sendEmptyMessage(0);
                            }
                            //턴 컨트롤
                            else if (tcp_type.equals("6.5")) {
                                //다이얼로그 띄우고 타입 6으로 보내기
                                dialoghandler.sendEmptyMessage(0);
                                Log.d("Chatting_tcp_type", tcp_type);
                            }
                            //게임이 끝났을때
                            else if (tcp_type.equals("0")) {
                                finish_mant = content;
                                dialogend.sendEmptyMessage(0);
                                Log.d("Chatting_tcp_type", tcp_type);
                            }
                            //도전자
                            else if (tcp_type.equals("7")) {
                                idx = content.indexOf("《");
                                to = content.substring(0, idx);
                                content = content.substring(idx + 1);
                                Log.d("Chatting is to", to);
                                Log.d("Chatting is ment", content);
                                Log.d("Chatting is tcp_type", tcp_type);
                                chatting.sendEmptyMessage(0);
                            }
                            //맞춤
                            else if (tcp_type.equals("7.5")) {
                                idx = content.indexOf("《");
                                to = content.substring(0, idx);
                                cassandra_answer  = content.substring(idx + 1);
//                                idx = content.indexOf("《");
//                                cassandra_answer = content.substring(idx + 1);
//                                content = content.substring(0, idx);
                                Log.d("Chatting is to", to);
                                Log.d("Chatting is ment", content);
                                Log.d("Chatting is tcp_type", tcp_type);
                                answer_chatting.sendEmptyMessage(0);
                            }
                            //게임 끝
                       /* else if (tcp_type.equals("7.8")){

                            answer_chatting.sendEmptyMessage(0);
                        }*/
                            //제한시간
                            else if (tcp_type.equals("8")) {
                                idx = content.indexOf("《");
                                time = content.substring(0, idx);
                                Log.d("timeout", time);
                                Log.d("idx_time" + tcp_type, String.valueOf(content));

                                if (time.equals("65")) {
                                    Log.d("timeout", "next");
                                }
                                timer.sendEmptyMessage(0);
                            }
                            //게임 끝남
                            else if (tcp_type.equals("11")) {
                                finishment.sendEmptyMessage(0);
                            }
                            //하트비트
                            else if (tcp_type.equals("13")) {
                                Log.d("1313", "13");
                                still_connect.sendEmptyMessage(0);
                            }
                            //술래가 소켓이 끊김
                            else if (tcp_type.equals("14")) {
                                Log.d("방장이 소켓 끊김", "ㄹㅇ");
                                taggerdisconnect.sendEmptyMessage(0);
                            }
                            //방에 혼자 남았을 떄
                            else if (tcp_type.equals("15")){
                                breakroom.sendEmptyMessage(0);
                            }
                            Log.d("Chatting is running", "2");

                        }
//                    Log.d("Chatting is running", "end");
                    } catch (SocketException e) {
                        Log.d("xxxxx", "xxx");
                        reconnect.sendEmptyMessage(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("xxxxx", "xxx222");
                        ment.sendEmptyMessage(0);

                    }
                    if (exit == true) {
                        Log.d("asd","tetetet");
                        break;
                    }
                }
                else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.d("asd","tetetetinterrupte");
                    }
                    Log.d("socket","disconnect");
                }
            }
            Log.d("Chatting is running", "2111");

        }
    };
    Handler ment = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(DrawActivity.this, "끊김...", Toast.LENGTH_SHORT).show();
        }
    };
    Handler breakroom = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            exit = true;
            Dialog("방에 나 혼자 남았습니다", "방을 나갑니다    ",2);
        }
    };
    Handler taggerdisconnect = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            item.add(new DrawData(content + "님이 방을 나가셨습니다." , "aa"));
            fabricView.cleanPage();
            FabricSetColor("BLACK");
            FabricSetThick("1");
            FabricsetDrawMode("BLACK","1");
            drawAdapter.notifyDataSetChanged();
        }
    };
    Handler reconnect = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(DrawActivity.this, "소켓 잠깐 끊김", Toast.LENGTH_SHORT).show();
            try {
                String test = new Tcp_connect(DrawActivity.this).execute("8000",room_num + "《" + id + "《" + tagger_or_not).get();
//                        run = true;
//                        checkUpdate.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Toast.makeText(DrawActivity.this, "소켓 잠깐 끊김", Toast.LENGTH_SHORT).show();
            draw_color = (String) choice_color.getSelectedItem();
            FabricSetColor(draw_color);
            FabricSetThick(thick);
        }
    };
    Handler still_connect = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tcp_chat = new Tcp_chat();
            tcp_chat.execute(id ,"13《" + room_num + "《" + id + "");
        }
    };
    Handler try_get_answer = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(answer_ed.getWindowToken(), 0);
            */
            blind.setVisibility(View.VISIBLE);
            // 비지블
            insert.setVisibility(View.VISIBLE);
            modify.setVisibility(View.GONE);
            fabricView.cleanPage();
            FabricSetColor("BLACK");
            FabricSetThick("1");
            FabricsetDrawMode("BLACK","1");
        }
    };
    Handler finishment = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            exit = true;
            Toast.makeText(DrawActivity.this, "게임이 끝났습니다.", Toast.LENGTH_SHORT).show();
        }
    };
    Handler dialoghandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(DrawActivity.this, "그릴 준비 하세요", Toast.LENGTH_SHORT).show();
            Dialog("술래입니다.","준비되셧나요?",1);
            fabricView.cleanPage();
            FabricSetColor("BLACK");
            FabricSetThick("1");
            FabricsetDrawMode("BLACK","1");
        }
    };
    Handler dialogend = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*result = new ArrayList<>();
            if (content.contains("《")){
                idx = content.indexOf("《");
            }*/
            String result = finish_mant.replace("《", "\n");
//            Toast.makeText(DrawActivity.this, "게임오버", Toast.LENGTH_SHORT).show();
            Dialog("게임이 끝났습니다.",result + "감사합니다",3);
        }
    };

    Handler game_chatting = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("test",String.valueOf(item.size()));
            item.add(new DrawData(to+ " : " + content , "aa"));
            drawAdapter.notifyDataSetChanged();

        }
    };
    Handler answer_chatting = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            Toast.makeText(DrawActivity.this, to + "님이 [" + cassandra_answer +"]를 맞추었습니다", Toast.LENGTH_SHORT).show();
//            Dialog("정답입니다.",to + "님이 맞추었습니다.",2);
            item.add(new DrawData(to + "님이 [" + cassandra_answer +"]를 맞추었습니다",""));
            drawAdapter.notifyDataSetChanged();
/*            FabricSetColor("BLACK");
            FabricSetThick("1");
            FabricsetDrawMode("BLACK","1");*/
            fabricView.cleanPage();
            FabricSetColor("BLACK");
            FabricSetThick("1");
            FabricsetDrawMode("BLACK","1");
            
        }
    };

    Handler chatting = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("test",String.valueOf(item.size()));
            item.add(new DrawData(to+" : " + content + "  ",""));
//            modify.setVisibility(View.GONE);
            drawAdapter.notifyDataSetChanged();

        }
    };


    Handler drawing = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            draw_color = (String) choice_color.getSelectedItem();
//            FabricSetColor(draw_color);
//            thick = (String) choice_thick.getSelectedItem();
//            FabricSetThick(thick);
            String con = "";
            int data_length;
            String rocking_tcp_type = tcp_type;
            Log.d("idx_111",String.valueOf(content) + "  "+ tcp_type);
            if (tcp_type.equals("3")){

                con = start_draw;
            }
            else if (tcp_type.equals("4")){
                con = middle_draw;
            }
            else if (tcp_type.equals("5")){
                con = end_draw;
            }

            data_length = getStringNumber(con);
            Log.d("data_length",content+ "  "+ tcp_type);
            Log.d("data_length",String.valueOf(data_length));

            Log.d("content1",con+ "  "+ tcp_type);
            if (data_length >= 8){
                Log.d("content1",con+ "  "+ tcp_type);
                idx = con.indexOf("《");

                Log.d("content1",con+ "  "+ tcp_type);
                color = con.substring(idx + 1);

                Log.d("content1",con+ "  "+ tcp_type);
                user_name = con.substring(0,idx);

                Log.d("content1",con+ "  "+ tcp_type);
                //색깔
                idx = color.indexOf("《");
                Log.d("idx_1", String.valueOf(idx));
                Log.d("idx_1color",String.valueOf(color));

                if (!color.contains(("《"))){
                    Log.d("error","this protocal has an erraor");
                }
                else {
                    thick = color.substring(idx + 1);
                    color = color.substring(0, idx);
                    //두께
                    idx = thick.indexOf("《");
                    draw_erase = thick.substring(idx + 1);
                    thick = thick.substring(0, idx);
                    Log.d("aaaa", draw_erase);
                    //타입
                    if (!draw_erase.contains(("《"))){
                        Log.d("error","this protocal has an erraor");
                    }
                    else {
                        idx = draw_erase.indexOf("《");
                        a1 = draw_erase.substring(idx + 1);
                        draw_erase = draw_erase.substring(0, idx);
                        //술래 사이즈 x 크기
                        idx = a1.indexOf("《");
                        b1 = a1.substring(idx + 1);
                        a1 = a1.substring(0, idx);
                        //술래 사이즈 y 크기
                        idx = b1.indexOf("《");
                        x1 = b1.substring(idx + 1);
                        b1 = b1.substring(0, idx);
                        //X좌표
                        idx = x1.indexOf("《");
                        y1 = x1.substring(idx + 1);
                        x1 = x1.substring(0, idx);
                        //Y좌표
                        idx = y1.indexOf("《");
                        y1 = y1.substring(0, idx);
                    }
                    Log.d("info", room_num + " " + user_name + " " + color + " " + thick + " " + draw_erase + " " + a1 + " " + b1 + " " + x1 + " " + y1 + "||");
                    Log.d("1111111tcp_type", tcp_type);
                    Log.d("x,y", x1 + " ||| " + y1);
                    if (!user_name.equals(id)) {
                        //그림좌표
                        x = Float.parseFloat(x1);
                        y = Float.parseFloat(y1);
                        //그림크기
                        c = Float.parseFloat(a1);
                        d = Float.parseFloat(b1);

                        x = (a/c) * x;
                        y = (b/d) * y;
                        long downTime = SystemClock.uptimeMillis();
                        long eventTime = SystemClock.uptimeMillis();

                        switch (color){
                            case "BLACK":
                                fabricView.setColor(Color.BLACK);
                                break;
                            case "BLUE":
                                fabricView.setColor(Color.BLUE);
                                break;
                            case "RED":
                                fabricView.setColor(Color.RED);
                                break;
                            case "GREEN":
                                fabricView.setColor(Color.GREEN);
                                break;
                        }
                        FabricSetThick(thick);
                        FabricsetDrawMode(draw_erase,color);
                        Log.d("tycp_type", tcp_type);
                        Log.d("x,,,y", "" + x + "  "+ y +"  " + downTime +"   "+ eventTime);
                        if (rocking_tcp_type.equals("3")) {
                            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
                            fabricView.onTouchDrawMode(event);
                        } else if (rocking_tcp_type.equals("4")) {
                            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, 0);
                            fabricView.onTouchDrawMode(event);
                        } else if (rocking_tcp_type.equals("5")) {
                            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
                            fabricView.onTouchDrawMode(event);
                        }

                        Log.d("boolean", tcp_type);
                    } else
                        Log.d("info", "dksemfdjrksek");
                }
            }
            else
                Log.d("length_draw", String.valueOf(data_length));


        }
    };
    Handler drawing_form = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            blind.setVisibility(View.GONE);   //비지블
            tagger_or_not = "3";
            modify.setVisibility(View.VISIBLE);
            blind.setVisibility(View.GONE);
            insert.setVisibility(View.GONE); //비지블
            fabricView.cleanPage();
            FabricSetColor("BLACK");
            FabricSetThick("1");
            FabricsetDrawMode("BLACK","1");
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(answer_ed.getWindowToken(), 0);
            if (answer.length() > 3){
                answer_view.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
            }
            else {
                answer_view.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
            }
//            Toast.makeText(DrawActivity.this, answer, Toast.LENGTH_SHORT).show();
            answer_view.setText(answer);
            fabricView.refreshDrawableState();
            Dialog("술래입니다.","준비되셧나요?",1);
        }
    };
    Handler timer = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (time.equals("60")){
                timer_view.setText("1:00");
            }
            if (time.equals("65")){
                my_status = "challenger";
                timer_view.setText("over");
                fabricView.cleanPage();
                FabricSetColor("BLACK");
                FabricSetThick("1");
                FabricsetDrawMode("BLACK","1");
//                Toast.makeText(DrawActivity.this, "제한시간 끝", Toast.LENGTH_SHORT).show();
//                blind.setVisibility(View.VISIBLE);
            }
            else {
                timer_view.setText("0:"+time);
            }
        }
    };
    int getStringNumber(String str){
        int count = 0;
        int idx = 0;
        Log.d(";...stlqkf",str);
        while(true){
            Log.d("satrrr",String.valueOf(count));

            if (str.contains("《")){
                count = count + 1;
                idx = str.indexOf("《");
                str = str.substring(idx + 1);
            }
            else {
                break;
            }

        }

        return count;
    }




    @Override
    public boolean onTouch(View v, MotionEvent event) {

        String str = null;
        String content;
        Log.d("my status", my_status);
        if (my_status.equals("tagger")) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    str = "Coordinate1 : ( " + (int) x + ", " + (int) y + " )";
                    Log.d("bbbbb", String.valueOf(draw_type));
                    content = "3《" + room_num + "《" + id + "《" + draw_color + "《" + thick + "《" + draw_erase + "《" + a + "《" + b + "《" + x + "《" + y + "《";
                    new Tcp_chat().execute(id, content);
                    draw_type = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    x = event.getX();
                    y = event.getY();
                    content = "4《" + room_num + "《" + id + "《" + draw_color + "《" + thick + "《" + draw_erase + "《" + a + "《" + b + "《" + x + "《" + y + "《";
                    new Tcp_chat().execute(id, content);
                    break;
                case MotionEvent.ACTION_UP:
                    content = "5《" + room_num + "《" + id + "《" + draw_color + "《" + thick + "《" + draw_erase + "《" + a + "《" + b + "《" + x + "《" + y + "《";
                    new Tcp_chat().execute(id, content);
                    break;
            }
        }
        else {
//            Toast.makeText(this, ",,,,", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    void FabricSetColor(String color){
        switch (color){
            case "BLACK":
//                Toast.makeText(this, "블랙", Toast.LENGTH_SHORT).show();
                fabricView.setColor(Color.BLACK);
                break;
            case "BLUE":
//                Toast.makeText(this, "블루", Toast.LENGTH_SHORT).show();
                fabricView.setColor(Color.BLUE);
                break;
            case "RED":
//                Toast.makeText(this, "레드", Toast.LENGTH_SHORT).show();
                fabricView.setColor(Color.RED);
                break;
            case "GREEN":
//                Toast.makeText(this, "그린", Toast.LENGTH_SHORT).show();
                fabricView.setColor(Color.GREEN);
        }
    }
    void FabricSetThick(String think){
        int thinc_int = Integer.parseInt(think);
        fabricView.setSize(5 * thinc_int);

    }
    void FabricsetDrawMode(String draw_erase,String color){
        switch (draw_erase){
            case "1":
                FabricSetColor(color);
                break;
            case "2":
                fabricView.setColor(Color.rgb(235,236,237));
                break;
        }
    }
    public void Dialog(String title, String message, final int type){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (type == 1) {
                    content = "6《" + room_num + "《" + id + "《";
//                    new Tcp_chat().execute(id, content);
                    blind.setVisibility(View.GONE);
                    /*InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(answer_ed.getWindowToken(), 0);*/
                    insert.setVisibility(View.GONE); //비지블
                    fabricView.cleanPage();
                }
                else if (type == 3){
                    new Tcp_chat().execute(id ,"10《" + room_num + "《" + id + "》");
                    finish();
                }
                else if (type == 2){
                    new Tcp_chat().execute(id ,"10《" + room_num + "《" + id + "》");
                    finish();
                }
            }
        });
        dialog.show();
    }   

}