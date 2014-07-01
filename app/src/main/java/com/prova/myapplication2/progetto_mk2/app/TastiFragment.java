package com.prova.myapplication2.progetto_mk2.app;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.prova.myapplication2.progetto_mk2.app.async.SetSpeedAsynk;
import com.prova.myapplication2.progetto_mk2.app.async.SetSteeringAsynk;

/**
 * Created by Monica on 03/06/2014.
 */
public class TastiFragment extends android.support.v4.app.Fragment implements View.OnTouchListener {

    View view;

    Button leftbtn, rightbtn, upbtn, downbtn;
    TextView textTasti, textLimSter, textLimVel;
    SeekBar limSpeed, limSter;
    int speed, sterzo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_drive, container, false);
        leftbtn = (Button) view.findViewById(R.id.buttonLeft);
        rightbtn = (Button) view.findViewById(R.id.buttonRight);
        upbtn = (Button) view.findViewById(R.id.buttonUP);
        downbtn = (Button) view.findViewById(R.id.buttonDOWN);
        limSpeed = (SeekBar) view.findViewById(R.id.seekBarLimiterVel);
        limSter = (SeekBar) view.findViewById(R.id.seekBarLimiterSterzo);
        textLimSter = (TextView) view.findViewById(R.id.textViewLimiterSterzo);
        textLimVel = (TextView) view.findViewById(R.id.textViewLimiterSpeed);
        speed = limSpeed.getProgress();
        textLimVel.setText("Velocità: " + speed);
        sterzo = limSter.getProgress();
        textLimSter.setText("Sterzo: " + sterzo);

        leftbtn.setOnTouchListener(this);
        rightbtn.setOnTouchListener(this);
        upbtn.setOnTouchListener(this);
        downbtn.setOnTouchListener(this);

        limSter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textLimSter.setText("Sterzo: " + progress);
                sterzo = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        limSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textLimVel.setText("Velocità: " + progress);
                speed = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        textTasti = (TextView) view.findViewById(R.id.textTasti);

        return view;


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.equals(upbtn)) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                textTasti.setText("Velocity set:" + (speed));
                Log.e("creazione ASYNCSETSPEED", "Eseguito");
                new SetSpeedAsynk().execute(speed);
            }
            if (event.getAction() == MotionEvent.ACTION_UP){
                textTasti.setText("UP STOPPED");
                new SetSpeedAsynk().execute(0);
            }
        }

        if (v.equals(downbtn)) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                textTasti.setText("Velocity set:" + (-speed));
                Log.e("creazione ASYNCSETSPEED", "Eseguito");
                new SetSpeedAsynk().execute(-speed);
            }
             if (event.getAction() == MotionEvent.ACTION_UP){
                 textTasti.setText("DOWN STOPPED");
                 new SetSpeedAsynk().execute(0);
             }


        }

        if (v.equals(leftbtn)) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                textTasti.setText("LEFT: " + sterzo);
                new SetSteeringAsynk().execute(sterzo);
            }
            if (event.getAction()==MotionEvent.ACTION_UP){

                textTasti.setText("LEFT rilasciato");
                new SetSteeringAsynk().execute(0);
            }
        }


        if (v.equals(rightbtn)) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                textTasti.setText("RIGHT: " + (-sterzo));
                new SetSteeringAsynk().execute(-sterzo);
            } else {
                if (event.getAction() == MotionEvent.ACTION_UP);
                textTasti.setText("RIGHT rilasciato");
                new SetSteeringAsynk().execute(0);
            }
        }


        return true
                ;
    }

}
