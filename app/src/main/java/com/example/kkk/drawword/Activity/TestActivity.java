package com.example.kkk.drawword.Activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agsw.FabricView.FabricView;
import com.byox.drawview.enums.DrawingCapture;
import com.byox.drawview.enums.DrawingMode;
import com.byox.drawview.views.DrawView;
import com.example.kkk.drawword.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-10-18.
 */

public class TestActivity extends Activity implements View.OnTouchListener {
    @BindView(R.id.draw_view) DrawView drawView;
    @BindView(R.id.button1)
    Button btn1;
    @BindView(R.id.button2) Button btn2;
    @BindView(R.id.textView2)
    TextView textView;
    @BindView(R.id.textView1)
    TextView textView2;
    @BindView(R.id.lin)LinearLayout linearLayout;
/*    @BindView(R.id.imageView)
    ImageView image;*/
    //    @BindView(R.id.faricView)
//    FabricView fabricView;
//    float x = event.getX(), y;
    float x;
    float y;
    int ty = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testayout);
        ButterKnife.bind(this);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawView.setDrawingMode(DrawingMode.DRAW);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawView.setDrawWidth(20);
                drawView.setDrawingMode(DrawingMode.ERASER);
            }
        });
//        drawView.setEnabled(false);
        linearLayout.setOnTouchListener(this);


//        fabricView.setColor(Color.BLUE);
//        fabricView.setOnTouchListener(this);
//        drawView.setOnTouchListener(this);
        drawView.setBackgroundColor(Color.WHITE);
        drawView.setDrawColor(Color.BLUE);
        thread.start();

//        float x = fabricView.getWidth();
//        float y = fabricView.getHeight();
        final int[] a = {0};
        textView2.setText("x : " + x + " y1 : " + y);
//        MotionEvent event;
        final MotionEvent finalEvent = null;
        final String[] str = new String[1];
//        int a = 1;
        drawView.setOnDrawViewListener(new DrawView.OnDrawViewListener() {
            @Override
            public void onStartDrawing() {
                // Your stuff here

//                Toast.makeText(TestActivity.this, "Start", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onEndDrawing() {
                // Your stuff here
                Paint paint = drawView.getCurrentPaintParams();

//                Toast.makeText(TestActivity.this, "end", Toast.LENGTH_SHORT).show();
                drawView.createCapture(DrawingCapture.BITMAP);
//                image.setImageBitmap(a);
//                y = drawView.getTranslationY();
//                Toast.makeText(TestActivity.this, ""+ x+ " , "+ y, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onClearDrawing() {
                // Your stuff here
                Toast.makeText(TestActivity.this, "clear", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onRequestText() {
                // Your stuff here
                Toast.makeText(TestActivity.this, "request", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAllMovesPainted() {
                // Your stuff here
                /*x = drawView.getWidth();
                y = drawView.getHeight();*/
//                Log.d("location",""+ x+ " , "+ y );
                // 좌표값을 이용하여 문자열을 구성한다.
                str[0] = "Coordinate4 : ( " + (int)x + ", " + (int)y + " )"  + ty;
                Log.d("ttttttt",str[0]);
                // 구성한 문자열을 텍스트뷰에 출력한다.
//                Toast.makeText(this, String.valueOf(a), Toast.LENGTH_SHORT).show();

                textView.setText(str[0]);
//                Toast.makeText(TestActivity.this, "allmove", Toast.LENGTH_SHORT).show();
            }
        });

    }
    int a = 0;
    public boolean onTouch(View v, MotionEvent event)
    {


        String str = null;
        // 좌표값을 이용하여 문자열을 구성한다.
        // 구성한 문자열을 텍스트뷰에 출력한다.
        textView.setText(str);
        // 어떤 이벤트가 발생하였는지에 따라 처리가 달라진다.
        switch (event.getAction()) {
            // DOWN 이나 MOVE 가 발생한 경우
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                ty= 2;
                // 좌표값을 이용하여 문자열을 구성한다.
                str = "Coordinate1 : ( " + (int)x + ", " + (int)y + " )";
                // 구성한 문자열을 텍스트뷰에 출력한다.
                textView.setText(str);
                break;
            case MotionEvent.ACTION_MOVE:
                // 터치가 발생한 X, Y 의 각 좌표를 얻는다.
                x = event.getX();
                y = event.getY();
                ty= 3;
                // 좌표값을 이용하여 문자열을 구성한다.
                str = "Coordinate2 : ( " + (int)x + ", " + (int)y + " )";
                // 구성한 문자열을 텍스트뷰에 출력한다.
//                Toast.makeText(this, String.valueOf(a), Toast.LENGTH_SHORT).show();
                a = a + 1;
                textView.setText(str);
                break;
            case MotionEvent.ACTION_UP:
                // UP 이 발생한 경우 문자를 출력한다.
                textView.setText("Touch Touch!!");
                break;
        }
        // false 를 반환하여 뷰 내에 재정의한 onTouchEvent 메소드로 이벤트를 전달한다.
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN:
                // 처음 터치가 눌러졌을 때
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 터치가 눌린 상태에서 움직일 때
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                // 터치가 떼어졌을 때
                x = event.getX();
                y = event.getY();
                break;
            default :
                break;
        }
        return true;

    }
    private Handler handler = new Handler(){

    };
    Thread thread = new Thread(){
        @Override
        public void run() {
            super.run();
            Instrumentation inst = new Instrumentation();
            long downTime = SystemClock.uptimeMillis();
            long eventTime = SystemClock.uptimeMillis();
            for (int i = 0; i < 500; i++) {
                MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, 400+i,400+i*2, 0);
                inst.sendPointerSync(event);

                Log.d("toast","???");
            }
            for (int i = 0; i < 0; i--) {
                MotionEvent event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, 400+i,400+i, 0);
                inst.sendPointerSync(event);

                Log.d("toast","???");
            }
            Log.d("toast","???");
        }
    };

}

