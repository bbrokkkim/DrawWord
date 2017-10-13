package com.example.kkk.drawword;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by KKK on 2017-10-14.
 */

public class MyView extends View {
    public MyView(Context context, AttributeSet attrs) {

        super(context, attrs);

    }



    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        //Canvas가 보유한 여러가지 기능 테스트



        //사각형 그리기

        Paint paint = new Paint();

        paint.setColor(Color.GREEN);



        canvas.drawRect(50, 100, 50+1000, 100+200, paint);





        //아이콘 그리기

        //비트맵 이미지 객체 이용

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);



        for(int i=0;i<15;i++){

            canvas.drawBitmap(bitmap, 200+(i*20), 500+(i*20), null);

        }



        //텍스트 그리기

        Paint paint2 = new Paint();

        paint2.setColor(Color.BLUE);

        paint2.setTextSize(80);

        canvas.drawText("텍스트입니다.", 200, 200, paint2);



        //선 그리기

        Paint paint3 = new Paint();

        paint3.setColor(Color.BLACK);



        canvas.drawLine(200, 500, 500, 600, paint3);



    }

}
