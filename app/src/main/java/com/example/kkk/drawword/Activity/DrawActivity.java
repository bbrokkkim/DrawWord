package com.example.kkk.drawword.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agsw.FabricView.FabricView;
import com.example.kkk.drawword.Adapter.DrawAdapter;
import com.example.kkk.drawword.Data.ChatData;
import com.example.kkk.drawword.Data.DrawData;
import com.example.kkk.drawword.IntentClass;
import com.example.kkk.drawword.R;
import com.example.kkk.drawword.SocketGet;
import com.example.kkk.drawword.Tcp_chat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-10-13.
 */

public class DrawActivity extends Activity implements View.OnTouchListener{
    @BindView(R.id.draw_listview) ListView listView;
    @BindView(R.id.game_submit) Button submit;
    @BindView(R.id.answer_content) EditText answer_ed;
    @BindView(R.id.answer) TextView answer_view;/*
    @BindView(R.id.textView2) TextView textView;
    @BindView(R.id.textView1) TextView textView2;*/
    @BindView(R.id.timer) TextView timer_view;
//    @BindView(R.id.lin) LinearLayout linearLayout;
    @BindView(R.id.blind) Button blind;
    @BindView(R.id.faricView) FabricView fabricView;
    @BindView(R.id.choice_color) Spinner choice_color;
    @BindView(R.id.choice_thick) Spinner choice_thick;
    @BindView(R.id.modify) LinearLayout modify;
    @BindView(R.id.linear) LinearLayout linear;

    @BindView(R.id.draworerase) ImageButton draw_or_erase;
    DrawAdapter drawAdapter;
    ArrayList<DrawData> item;
    //tcp
    BufferedWriter bufferedWriter;
    BufferedReader bufferedReader;
    Socket socket;

    Boolean draw_or_erase_boolean = true;
    float x,y;
    String content, to;
    String id = null;
    int idx;
    String room_num = "";
    String user_name = "";
    String tcp_type = "";
    String thick = "";
    String color = "";
    String x1 = "";
    String y1 = "";
    String answer = "";
    String time = "";
    String draw_erase = "1";
    String status = "";
    SocketGet socketGet = SocketGet.getInstance();
    boolean draw_type;
    MotionEvent event;
    IntentClass intentClass = new IntentClass(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_layout);
        ButterKnife.bind(this);
        timer_view.setText("aaasdsddf");
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        room_num = intent.getStringExtra("room_num");
        status = intent.getStringExtra("status");
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
        item = new ArrayList<>();
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        drawAdapter = new DrawAdapter(getLayoutInflater(),item);
        listView.setAdapter(drawAdapter);
        modify.bringToFront();
        socket = socketGet.getSocket();
        bufferedReader = socketGet.getBufferedReader();
        bufferedWriter = socketGet.getBufferedWriter();
        Toast.makeText(this, String.valueOf(socketGet.getA()), Toast.LENGTH_SHORT).show();

        if (status.equals("1")) {
            blind.setVisibility(View.GONE);
//            content =
            Dialog();
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
//            new Tcp_chat().execute(id,content);
        }
        else if (status.equals("2")){
            blind.setVisibility(View.VISIBLE);
        }
        checkUpdate.start();

        fabricView.setOnTouchListener(this);
        linear.setOnTouchListener(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = answer_ed.getText().toString();
//                item.add(new DrawData(answer,"aa"));
                Toast.makeText(DrawActivity.this, answer, Toast.LENGTH_SHORT).show();

                String push_content = "1《" + room_num + "《" + id + "》" + answer;
                new Tcp_chat().execute(id ,push_content);

                drawAdapter.notifyDataSetChanged();
                answer_ed.setText("");
            }
        });
        draw_or_erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DrawActivity.this, "지우기", Toast.LENGTH_SHORT).show();
                if (draw_or_erase_boolean == true){
                    draw_or_erase.setImageResource(R.mipmap.ic_launcher);
                    fabricView.setColor(Color.WHITE);
                    draw_or_erase_boolean = false;
                    draw_erase = "2";
                    Toast.makeText(DrawActivity.this, "지우기", Toast.LENGTH_SHORT).show();
                }
                else {
                    draw_or_erase.setImageResource(R.mipmap.ic_launcher_erase);
                    FabricSetColor(color);
                    draw_or_erase_boolean = true;
                    draw_erase = "1";
                    Toast.makeText(DrawActivity.this, color, Toast.LENGTH_SHORT).show();
                }
            }
        });
        choice_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color = (String) choice_color.getSelectedItem();
                FabricSetColor(color);
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

    }

    private Thread checkUpdate = new Thread() {

        public void run() {
            String line = null;
            Log.w("ChattingStart", "Start Thread");

            content = null;
            boolean test = false;
            try {
                while ((line = bufferedReader.readLine()) !=null) {
                    Log.w("Chatting is running" , "1");
                    Log.d("line", line);

                    if (!line.contains("《")){
                        Log.w("Chatting is error" , "error");
                        continue;
                    }

                    int idx_ment = line.indexOf("《");
                    String real_ment = line.substring(idx_ment + 1);
                    Log.d("made_line", real_ment);
                    int idx = real_ment.indexOf("《");
                    Log.d("chatting Test", String.valueOf(idx));
                    tcp_type = real_ment.substring(0, idx);
                    content = real_ment.substring(idx+1);
                    Log.d("Chatting is content", content);
                    Log.d("Chatting is tcp_type", tcp_type);

                    //chatting
                    if (tcp_type.equals("1")){
                        idx = content.indexOf("》");
                        to = content.substring(0,idx);
                        content = content.substring(idx+1);
                        chatting.sendEmptyMessage(0);
                        Log.d("Chatting is to", to);
                        Log.d("Chatting is ment", content);
                    }

                    //user_list_status
                    else if (tcp_type.equals("2")) {
                        game_chatting.sendEmptyMessage(0);
                    }

                    else if (tcp_type.equals("3")){

                        Log.d("Chatting is tcp_type", tcp_type);
                        Log.d("bb",String.valueOf(draw_type));
                        drawing.sendEmptyMessage(0);
                    }

                    else if (tcp_type.equals("4")){
                        drawing.sendEmptyMessage(0);
                        Log.d("Chatting is tcp_type", tcp_type);
                        Log.d("bb",String.valueOf(draw_type));
                    }

                    else if (tcp_type.equals("5")){
                        drawing.sendEmptyMessage(0);
                        Log.d("Chatting is tcp_type", tcp_type);
                        Log.d("bb",String.valueOf(draw_type));
                    }
                    //술래
                    else if (tcp_type.equals("6")){
                        //유저네임
                        idx = content.indexOf("《");
                        answer = content.substring(idx + 1);
                        user_name = content.substring(0,idx);
                        //답
                        idx = answer.indexOf("《");
                        answer = answer.substring(0,idx);
                        drawing_form.sendEmptyMessage(0);
                        Log.d("Chatting is tcp_type", tcp_type);
                    }
                    //턴 컨트롤
                    else if (tcp_type.equals("6.5")){
                        //다이얼로그 띄우고 타입 6으로 보내기
                        dialoghandler.sendEmptyMessage(0);
                    }
                    //맞추기
                    else if (tcp_type.equals("7")){
                        idx = content.indexOf("《");
                        user_name = content.substring(0,idx);
                        Log.d("Chatting is tcp_type", tcp_type);
                        try_form.sendEmptyMessage(0);
                    }
                    //제한시간
                    else if (tcp_type.equals("8")){
                        idx = content.indexOf("《");
                        time = content.substring(0,idx);
                        Log.d("time",time);
                        if (time.equals("65")){
                            Log.d("timeout","next");
                        }
                        timer.sendEmptyMessage(0);
                    }

//                    ment = real_ment.substring(idx + 1);
                    Log.d("Chatting is running", "2");

                }
                Log.d("Chatting is running", "end");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Chatting is running", "2111");

        }
    };
    Handler dialoghandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(DrawActivity.this, "그릴 준비 하세요", Toast.LENGTH_SHORT).show();
            Dialog();
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

    Handler chatting = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("test",String.valueOf(item.size()));

            item.add(new DrawData(to+" : " + content + "  ",""));
            drawAdapter.notifyDataSetChanged();

        }
    };


    Handler drawing = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            idx = content.indexOf("《");
            color = content.substring(idx + 1);
            user_name = content.substring(0,idx);
            //색깔
            idx = color.indexOf("《");
            thick = color.substring(idx+1);
            color = color.substring(0,idx);
            //두께
            idx = thick.indexOf("《");
            draw_erase = thick.substring(idx+1);
            thick = thick.substring(0,idx);
            Log.d("aaaa",draw_erase);
            //타입
            idx = draw_erase.indexOf("《");
            x1 = draw_erase.substring(idx+1);
            draw_erase = draw_erase.substring(0,idx);
            //X좌표
            idx = x1.indexOf("《");
            y1 = x1.substring(idx+1);
            x1 = x1.substring(0,idx);
            //Y좌표
            idx = y1.indexOf("《");
            y1 = y1.substring(0,idx);

            Log.d("info",room_num + " " + user_name +" " + color +" " + thick + " " + draw_erase +" " + x1 + " " + y1 + "||" );
            Log.d("1111111tcp_type",tcp_type);
            Log.d("x,y", x1 + " ||| " + y1);
            if (!user_name.equals(id)){
                x = Float.parseFloat(x1);
                y = Float.parseFloat(y1);

                long downTime = SystemClock.uptimeMillis();
                long eventTime = SystemClock.uptimeMillis();

                FabricSetColor(color);
                FabricSetThick(thick);
//                FabricsetDrawMode("1");
                if (tcp_type.equals("3")){
                    Toast.makeText(DrawActivity.this, "타입 3", Toast.LENGTH_SHORT).show();
                    event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x,y, 0);
                }
                else if (tcp_type.equals("4")){
                    Toast.makeText(DrawActivity.this, "타입 4", Toast.LENGTH_SHORT).show();
                    event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, 0);
                }
                else if (tcp_type.equals("5")){
                    Toast.makeText(DrawActivity.this, "타입 5", Toast.LENGTH_SHORT).show();
                    event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
                }
                fabricView.onTouchDrawMode(event);
                Log.d("boolean",tcp_type);
            }
            else
                Log.d("info","dksemfdjrksek");
        }
    };
    Handler drawing_form = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            blind.setVisibility(View.GONE);
            answer_view.setText(answer);
            fabricView.refreshDrawableState();

        }
    };
    Handler try_form = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            fabricView.refreshDrawableState();
//            blind.setVisibility(View.VISIBLE);
        }
    };

    Handler timer = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            timer_view.setText(time);
            if (time.equals("65")){
                Toast.makeText(DrawActivity.this, "제한시간 끝", Toast.LENGTH_SHORT).show();
                blind.setVisibility(View.VISIBLE);
            }
        }
    };



    @Override
    public boolean onTouch(View v, MotionEvent event) {

        String str = null;
        String content;
        switch (event.getAction()) {
            // DOWN 이나 MOVE 가 발생한 경우
            case MotionEvent.ACTION_DOWN:
//                Toast.makeText(this, "시작", Toast.LENGTH_SHORT).show();
                x = event.getX();
                y = event.getY();
                // 좌표값을 이용하여 문자열을 구성한다.
                str = "Coordinate1 : ( " + (int) x + ", " + (int) y + " )";
                timer_view.setText(str);
                // 구성한 문자열을 텍스트뷰에 출력한다.
                Log.d("bb",String.valueOf(draw_type));
                content = "3《"+room_num+"《"+id+"《"+color+"《"+thick+"《"+draw_erase+"《"+x+"《"+y+"《";
                new Tcp_chat().execute(id,content);
                draw_type = true;
                break;
            case MotionEvent.ACTION_MOVE:
                // 터치가 발생한 X, Y 의 각 좌표를 얻는다.
                x = event.getX();
                y = event.getY();
                str = "Coordinate23 : ( " + (int) x + ", " + (int) y + " )";
                content = "4《"+room_num+"《"+id+"《"+color+"《"+thick+"《"+draw_erase+"《"+x+"《"+y+"《";
                new Tcp_chat().execute(id,content);
                timer_view.setText(str);
                break;
            case MotionEvent.ACTION_UP:
//                Toast.makeText(this, "끝", Toast.LENGTH_SHORT).show();
                // UP 이 발생한 경우 문자를 출력한다.
                content = "5《"+room_num+"《"+id+"《"+color+"《"+thick+"《"+draw_erase+"《"+x+"《"+y+"《";
                timer_view.setText(str);

                new Tcp_chat().execute(id,content);

                break;
        }
        return false;
    }

    void FabricSetColor(String color){
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
        }
    }
    void FabricSetThick(String think){
        int thinc_int = Integer.parseInt(think);
        fabricView.setSize(5 * thinc_int);

    }
    void FabricsetDrawMode(String draw_erase){
        switch (draw_erase){
            case "1":
                fabricView.setColor(Color.BLACK);
                break;
            case "2":
                fabricView.setColor(Color.WHITE);
                break;
        }
    }
    public void Dialog(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle("술래입니다.");
        dialog.setMessage("준비가 되셨습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                content = "6《"+ room_num + "《" + id + "《";
                new Tcp_chat().execute(id,content);
                blind.setVisibility(View.GONE);
            }
        });
        dialog.show();
    }   

}