package com.example.kkk.drawword;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.agsw.FabricView.FabricView;
import com.byox.drawview.enums.DrawingCapture;
import com.byox.drawview.enums.DrawingMode;
import com.byox.drawview.views.DrawView;
import com.example.kkk.drawword.Activity.DrawActivity;
import com.example.kkk.drawword.Activity.GameActivity;
import com.example.kkk.drawword.Activity.RoomActivity;
import com.example.kkk.drawword.Data.ChatData;
import com.example.kkk.drawword.Data.ReadyData;
import com.example.kkk.drawword.Okhttp.OkhttpFriend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-10-20.
 */

public class Test2Activity extends Activity implements View.OnTouchListener {
//    @BindView(R.id.draw_view)
//    DrawView drawView;
    @BindView(R.id.button1)
    Button btn1;
    @BindView(R.id.button2)
    Button btn2;
    @BindView(R.id.answer)
    TextView answer_view;
    @BindView(R.id.textView2)
    TextView textView;
    @BindView(R.id.textView1)
    TextView textView2;
    @BindView(R.id.timer)
    TextView timer_view;
    @BindView(R.id.lin)
    LinearLayout linearLayout;
    @BindView(R.id.blind)
    Button blind;
    @BindView(R.id.faricView)
    FabricView fabricView;
    @BindView(R.id.choice_color)
    Spinner choice_color;
    @BindView(R.id.choice_thick)
    Spinner choice_thick;
    @BindView(R.id.modify)
    LinearLayout modify;
    @BindView(R.id.draworerase)
    ImageButton draw_or_erase;
    Boolean draw_or_erase_boolean = true;
//    float x = event.getX(), y;
    float y;
    float x;
    String content, to;
    String id = null;
    int idx;
    String input ="";
    String room_num = "";
    String user_name = "";
    String tcp_type = "";
    String ready_check = "";
    String thick = "";
    String color = "";
    String x1 = "";
    String y1 = "";
    String answer = "";
    String time = "";
    String draw_erase = "1";
    SocketGet socketGet = SocketGet.getInstance();
    long downTime = SystemClock.uptimeMillis();
    long eventTime = SystemClock.uptimeMillis();
    boolean draw_type;
    MotionEvent event;
    IntentClass intentClass = new IntentClass(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testayout);
        ButterKnife.bind(this);
        Database database = new Database(this,"user_db", null,1);

        room_num = "9";
        draw_or_erase.setImageResource(R.mipmap.ic_launcher_erase);
        modify.bringToFront();


        id = intentClass.GetId(database);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                drawView.setDrawingMode(DrawingMode.DRAW);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                drawView.setDrawWidth(20);
//                drawView.setDrawingMode(DrawingMode.ERASER);
            }
        });
//        drawView.setEnabled(false);
        linearLayout.setOnTouchListener(this);
//        imageView.setVisibility(View.VISIBLE);
//        imageView.setOnTouchListener(this);
//        fabricView.setVisibility(View.GONE);


//        fabricView.setColor(Color.RED);
        fabricView.setOnTouchListener(this);
        linearLayout.setOnTouchListener(this);

/*        fabricView.setFocusable(false);
        fabricView.setEnabled(false);*/

        /*MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, 500,500, 0);
        fabricView.onTouchDrawMode(event);
        for (int i = 0; i < 500; i++) {
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, 400+i,400+i*2, 0);

            fabricView.onTouchDrawMode(event);
            Log.d("toast",String.valueOf(i));
        }
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, 500,500, 0);
        fabricView.onTouchDrawMode(event);
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, 500,500, 0);
        fabricView.onTouchDrawMode(event);
        for (int i = 200; i < 0; i--) {
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, 400 + i, 400 + i, 0);
//                inst.sendPointerSync(event);
            fabricView.onTouchDrawMode(event);
            Log.d("toast", String.valueOf(i));
        }
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, 500,500, 0);
        fabricView.onTouchDrawMode(event);*/

        textView2.setText("x : " + x + " y1 : " + y);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blind.setVisibility(View.GONE);
                color = (String) choice_color.getSelectedItem();

                Toast.makeText(Test2Activity.this,color , Toast.LENGTH_SHORT).show();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blind.setVisibility(View.VISIBLE);
            }
        });
        draw_or_erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Test2Activity.this, "지우기", Toast.LENGTH_SHORT).show();
                if (draw_or_erase_boolean == true){
                    draw_or_erase.setImageResource(R.mipmap.ic_launcher);
                    fabricView.setColor(Color.WHITE);
                    draw_or_erase_boolean = false;
                    draw_erase = "2";
                    Toast.makeText(Test2Activity.this, "지우기", Toast.LENGTH_SHORT).show();
                }
                else{
                    draw_or_erase.setImageResource(R.mipmap.ic_launcher_erase);
                    FabricSetThick(color);
                    draw_or_erase_boolean = true;
                    draw_erase = "1";
                    Toast.makeText(Test2Activity.this, color, Toast.LENGTH_SHORT).show();
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
        checkUpdate.start();
    }

    public boolean onTouch(View v, MotionEvent event) {


        String str = null;
        String content;
        // 좌표값을 이용하여 문자열을 구성한다.
        // 구성한 문자열을 텍스트뷰에 출력한다.
        textView.setText(str);
        // 어떤 이벤트가 발생하였는지에 따라 처리가 달라진다.

        switch (event.getAction()) {
            // DOWN 이나 MOVE 가 발생한 경우
            case MotionEvent.ACTION_DOWN:
                Toast.makeText(this, "시작", Toast.LENGTH_SHORT).show();
                x = event.getX();
                y = event.getY();
                // 좌표값을 이용하여 문자열을 구성한다.
                str = "Coordinate1 : ( " + (int) x + ", " + (int) y + " )";
                // 구성한 문자열을 텍스트뷰에 출력한다.
                textView.setText(str);
                Log.d("bb",String.valueOf(draw_type));
                textView.setText("Touch Touch!!");
                content = "3《"+room_num+"《"+id+"《"+color+"《"+thick+"《"+draw_erase+"《"+x+"《"+y+"《";
                new Tcp_chat().execute(id,content);
                draw_type = true;
                break;
            case MotionEvent.ACTION_MOVE:
                // 터치가 발생한 X, Y 의 각 좌표를 얻는다.
                x = event.getX();
                y = event.getY();
                str = "Coordinate23 : ( " + (int) x + ", " + (int) y + " )";
                textView.setText(str);
                content = "4《"+room_num+"《"+id+"《"+color+"《"+thick+"《"+draw_erase+"《"+x+"《"+y+"《";
                new Tcp_chat().execute(id,content);
                break;
            case MotionEvent.ACTION_UP:

                Toast.makeText(this, "끝", Toast.LENGTH_SHORT).show();
                // UP 이 발생한 경우 문자를 출력한다.
                content = "5《"+room_num+"《"+id+"《"+color+"《"+thick+"《"+draw_erase+"《"+x+"《"+y+"《";
                new Tcp_chat().execute(id,content);
                
                break;
        }
        return false;
    }

    Thread checkUpdate = new Thread() {

        public void run() {
            String line = null;
            Log.w("ChattingStart", "Start Thread");
            content = null;
            draw_type = true;
            try {
                while ((line = socketGet.getBufferedReader().readLine()) != null) {
//                    line = "《4《test1《1《2《73.03711《436.4297《";
                    Log.d("--------", "-----------------------------------");
                    Log.d("line1", String.valueOf(draw_type));
                    if (line.contains("《") == false){
                        Log.d("Chatting is error" , "error!!!!!");
                        continue;
                    }
                    Log.d("line2", line);
                    Log.w("Chatting is running" , "1");

                    int idx_ment = line.indexOf("《");
                    String real_ment = line.substring(idx_ment + 1);
                    Log.d("made_line", real_ment);
                    int idx = real_ment.indexOf("《");
                    Log.d("chatting Test", String.valueOf(idx));
                    tcp_type = real_ment.substring(0, idx);
                    content = real_ment.substring(idx+1);
                    Log.d("Chatting is content", content);
                    Log.d("Chatting is tcp_type", tcp_type);

//                    fabricView.setSize(10);
                    if (tcp_type.equals("3")){

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
                    else if (tcp_type.equals("6.5")){
                        //다이얼로그 띄우고 타입 6으로 보내기
                        Dialog();
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






    Handler drawing = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            idx = content.indexOf("《");
//            Log.d("aa",String.valueOf(idx));
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
            if (!user_name.equals(id)){
                x = Float.parseFloat(x1);
                y = Float.parseFloat(y1);

                long downTime = SystemClock.uptimeMillis();
                long eventTime = SystemClock.uptimeMillis();

                FabricSetColor(color);
                FabricSetThick(thick);
//                FabricsetDrawMode("1");
                if (tcp_type.equals("3")){
                    event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x,y, 0);
                }
                else if (tcp_type.equals("4")){
                    event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, 0);
                }
                else if (tcp_type.equals("5")){
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
            blind.setVisibility(View.GONE);
            answer_view.setText(answer);

        }
    };
    Handler try_form = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            blind.setVisibility(View.VISIBLE);
        }
    };

    Handler timer = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            timer_view.setText(time);
        }
    };
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
        switch (think){
            case "1":
                fabricView.setSize(3);
                break;
            case "2":
                fabricView.setSize(10);
                break;
            case "3":
                fabricView.setSize(15);
                break;
            case "4":
                fabricView.setSize(20);
                break;
        }
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



 /*   Thread thread = new Thread(){

        @Override
        public void run() {
            super.run();


            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    long downTime = SystemClock.uptimeMillis();
                    long eventTime = SystemClock.uptimeMillis();
                    MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, 500,500, 0);
                    fabricView.onTouchDrawMode(event);
                    for (int i = 0; i < 500; i++) {
                        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, 400+i,400+i*2, 0);

                        fabricView.onTouchDrawMode(event);
                        Log.d("toast",String.valueOf(i));
                    }
                    for (int i = 200; i < 0; i--) {
                        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, 400+i,400+i, 0);
//                inst.sendPointerSync(event);
                        fabricView.onTouchDrawMode(event);
                        Log.d("toast",String.valueOf(i));
                    }
                }
            });


            Log.d("toast","fin");
            *//*
            long downTime = SystemClock.uptimeMillis();
            long eventTime = SystemClock.uptimeMillis();
            MotionEvent event = MotionEvent.obtain(downTime+50, downTime+100,
                    MotionEvent.ACTION_UP, 400,650, 0);

            fabricView.onTouchDrawMode(event);*//*
        }
    };*/

    public void Dialog(){
        final AlertDialog.Builder dialog =
                new AlertDialog.Builder(this);
        dialog.setTitle("술래입니다.");
        dialog.setMessage("준비가 되셨습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                content = "《6《"+ room_num + "《" + user_name + "《";
                new Tcp_chat().execute(id,content);
            }
        });
        dialog.show();
    }
}