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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.example.kkk.drawword.Data.DrawData;
import com.example.kkk.drawword.Dialog;
import com.example.kkk.drawword.IntentClass;
import com.example.kkk.drawword.R;
import com.example.kkk.drawword.SocketGet;
import com.example.kkk.drawword.Tcp_chat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
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
    SocketGet socketGet = SocketGet.getInstance();
    boolean draw_type;
    MotionEvent event;
    IntentClass intentClass = new IntentClass(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_layout);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        room_num = intent.getStringExtra("room_num");
        status = intent.getStringExtra("status");
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

        if (status.equals("1")) {
            blind.setVisibility(View.GONE);
//            Dialog("술래입니다.","준비되셧나요?",1);
        }
        else if (status.equals("2")){
            blind.setVisibility(View.VISIBLE);
        }

        fabricView.setBackgroundColor(Color.rgb(255,224,193));
//        fabricView = new FabricView(this,);
//        fabricView.setBackground();
        fabricView.setOnTouchListener(this);
        linear.setOnTouchListener(this);
        fabricView.cleanPage();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = answer_ed.getText().toString();
                Toast.makeText(DrawActivity.this, answer+"tttteest", Toast.LENGTH_SHORT).show();
                if (!answer.equals("")) {
                    String push_content = "7《" + room_num + "《" + id + "《" + answer;
                    new Tcp_chat().execute(id, push_content);
                    drawAdapter.notifyDataSetChanged();
                    answer_ed.setText("");
                }
            }
        });

        draw_or_erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DrawActivity.this, "지우기", Toast.LENGTH_SHORT).show();
                if (draw_or_erase_boolean == true){
                    draw_or_erase.setImageResource(R.mipmap.ic_launcher);
                    fabricView.setColor(Color.rgb(255,224,193));
                    draw_or_erase_boolean = false;
                    draw_erase = "2";
//                    Toast.makeText(DrawActivity.this, "지우기", Toast.LENGTH_SHORT).show();
                }
                else {
                    draw_or_erase.setImageResource(R.mipmap.ic_launcher_erase);
                    FabricSetColor(color);
                    draw_or_erase_boolean = true;
                    draw_erase = "1";
//                    Toast.makeText(DrawActivity.this, color, Toast.LENGTH_SHORT).show();
                }
            }
        });
        choice_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                color = (String) choice_color.getSelectedItem();
                draw_color = (String) choice_color.getSelectedItem();
                FabricSetColor(draw_color);
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

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "게임이 시작되어 나갈수 없습니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this, "destroy", Toast.LENGTH_SHORT).show();
//        new Tcp_chat().execute(id ,"10《" + room_num + "《" + id + "》");

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        a = fabricView.getWidth();
        b = fabricView.getHeight();
        Toast.makeText(this, "" + a + " : " + b, Toast.LENGTH_SHORT).show();
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
                    Log.d("idx_1___",String.valueOf(line));
                    //chatting
                    if (tcp_type.equals("1")){
/*                        idx = content.indexOf("》");
                        to = content.substring(0,idx);
                        content = content.substring(idx+1);
                        chatting.sendEmptyMessage(0);
                        Log.d("Chatting is to", to);
                        Log.d("Chatting is ment", content);*/
                    }

                    //user_list_status
                    else if (tcp_type.equals("2")) {
//                        game_chatting.sendEmptyMessage(0);
                    }
                    //그리기
                    else if (tcp_type.equals("3")){

                        Log.d("Chatting is tcp_type", tcp_type);
                        Log.d("bb",String.valueOf(draw_type));
                        drawing.sendEmptyMessage(0);
                    }
                    //그리기
                    else if (tcp_type.equals("4")){
                        drawing.sendEmptyMessage(0);
                        Log.d("Chatting is tcp_type", tcp_type);
                        Log.d("bb",String.valueOf(draw_type));
                    }
                    //그리기
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
                    //도전자
                    else if (tcp_type.equals("6.1")){
                        try_get_answer.sendEmptyMessage(0);
                    }
                    //턴 컨트롤
                    else if (tcp_type.equals("6.5")){
                        //다이얼로그 띄우고 타입 6으로 보내기
                        dialoghandler.sendEmptyMessage(0);
                        Log.d("Chatting_tcp_type", tcp_type);
                    }
                    else if (tcp_type.equals("0")){
                        dialogend.sendEmptyMessage(0);
                        Log.d("Chatting_tcp_type", tcp_type);
                    }
                    //도전자
                    else if (tcp_type.equals("7")){
                        idx = content.indexOf("《");
                        to = content.substring(0,idx);
                        content = content.substring(idx+1);
                        Log.d("Chatting is to", to);
                        Log.d("Chatting is ment", content);
                        Log.d("Chatting is tcp_type", tcp_type);
                        chatting.sendEmptyMessage(0);
                    }
                    //맞춤
                    else if (tcp_type.equals("7.5")){
                        idx = content.indexOf("《");
                        to = content.substring(0,idx);
                        content = content.substring(idx+1);
                        idx = content.indexOf("《");
                        cassandra_answer = content.substring(idx+1);
                        content = content.substring(0,idx);
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
                    else if (tcp_type.equals("8")){
                        idx = content.indexOf("《");
                        time = content.substring(0,idx);
                        Log.d("timeout",time);
                        Log.d("idx_time"+tcp_type,String.valueOf(content));

                        if (time.equals("65")){
                            Log.d("timeout","next");
                        }
                        timer.sendEmptyMessage(0);
                    }
                    //게임 끝남
                    else if (tcp_type.equals("11")){
                        finish();
                        finishment.sendEmptyMessage(0);
                    }
                    Log.d("Chatting is running", "2");

                }
                Log.d("Chatting is running", "end");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Chatting is running", "2111");

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
            insert.setVisibility(View.VISIBLE);
            modify.setVisibility(View.GONE);
        }
    };
    Handler finishment = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(DrawActivity.this, "게임이 끝났습니다.", Toast.LENGTH_SHORT).show();
        }
    };
    Handler dialoghandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(DrawActivity.this, "그릴 준비 하세요", Toast.LENGTH_SHORT).show();
            Dialog("술래입니다.","준비되셧나요?",1);
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
            String result = content.replace("《", "\n");
            Toast.makeText(DrawActivity.this, "게임오버", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(DrawActivity.this, to + "님이 [" + cassandra_answer +"]를 맞추었습니다", Toast.LENGTH_SHORT).show();
//            Dialog("정답입니다.",to + "님이 맞추었습니다.",2);
            item.add(new DrawData(to + "님이 [" + cassandra_answer +"]를 맞추었습니다",""));
            drawAdapter.notifyDataSetChanged();
            
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
            Log.d("idx_111",String.valueOf(content));
            idx = content.indexOf("《");
            color = content.substring(idx + 1);
            user_name = content.substring(0,idx);
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
                    if (tcp_type.equals("3")) {
//                        Toast.makeText(DrawActivity.this, color, Toast.LENGTH_SHORT).show();
                        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
                    } else if (tcp_type.equals("4")) {
                        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, 0);
                    } else if (tcp_type.equals("5")) {
                        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
                    }
                    fabricView.onTouchDrawMode(event);
                    Log.d("boolean", tcp_type);
                } else
                    Log.d("info", "dksemfdjrksek");
            }
        }
    };
    Handler drawing_form = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            blind.setVisibility(View.GONE);
            modify.setVisibility(View.VISIBLE);
            blind.setVisibility(View.GONE);
            insert.setVisibility(View.GONE);
            fabricView.cleanPage();
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
                timer_view.setText("over");
                fabricView.cleanPage();
                Toast.makeText(DrawActivity.this, "제한시간 끝", Toast.LENGTH_SHORT).show();
                blind.setVisibility(View.VISIBLE);
            }
            else {
                timer_view.setText("0:"+time);
            }
        }
    };



    @Override
    public boolean onTouch(View v, MotionEvent event) {

        String str = null;
        String content;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                str = "Coordinate1 : ( " + (int) x + ", " + (int) y + " )";
                Log.d("bb",String.valueOf(draw_type));
                content = "3《"+room_num+"《"+id+"《"+draw_color+"《"+thick+"《"+draw_erase+"《"+a+"《"+b+"《"+x+"《"+y+"《";
                new Tcp_chat().execute(id,content);
                draw_type = true;
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                content = "4《"+room_num+"《"+id+"《"+draw_color+"《"+thick+"《"+draw_erase+"《"+a+"《"+b+"《"+x+"《"+y+"《";
                new Tcp_chat().execute(id,content);
                break;
            case MotionEvent.ACTION_UP:
                content = "5《"+room_num+"《"+id+"《"+draw_color+"《"+thick+"《"+draw_erase+"《"+a+"《"+b+"《"+x+"《"+y+"《";
                new Tcp_chat().execute(id,content);
                break;
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
                fabricView.setColor(Color.rgb(255,224,193));
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
                    insert.setVisibility(View.GONE);
                    fabricView.cleanPage();
                }
                else if (type == 3){
                    new Tcp_chat().execute(id ,"10《" + room_num + "《" + id + "》");
                    finish();
                }
            }
        });
        dialog.show();
    }   

}