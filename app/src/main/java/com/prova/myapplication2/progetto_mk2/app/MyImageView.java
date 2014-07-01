package com.prova.myapplication2.progetto_mk2.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

public class MyImageView extends ImageView{

    public MyImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawLine(0, 0, 20, 20, p);
    }

}