package com.prova.myapplication2.progetto_mk2.app;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.prova.myapplication2.progetto_mk2.app.async.SetSpeedAsynk;
import com.prova.myapplication2.progetto_mk2.app.async.SetSteeringAsynk;

/**
 * Created by Monica on 03/06/2014.
 */
public class SensorFragment extends android.support.v4.app.Fragment implements SensorEventListener {
    public SensorManager mSensorManager;
    Sensor mAccelerometer;

    MySingleton singleton = MySingleton.getInstance();
    ConnessionePi connessionePi = (ConnessionePi) singleton.hashMap.get("CONN");

    float values[];
    TextView textX, textY, textZ;
    ToggleButton toggleEnable;
    boolean usaSensori = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        View view = inflater.inflate(R.layout.fragment_accelerometer, container, false);
        textX = (TextView) view.findViewById(R.id.textX);
        textY = (TextView) view.findViewById(R.id.textY);
        textZ = (TextView) view.findViewById(R.id.textZ);
        toggleEnable = (ToggleButton) view.findViewById(R.id.toggleEnableMoveSensor);
        toggleEnable.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    usaSensori = true;
                    getView().setBackgroundColor(Color.GREEN);
                    textX.setTextColor(Color.BLUE);
                    textY.setTextColor(Color.BLUE);
                    textZ.setTextColor(Color.BLUE);
                    ((ToggleButton) v).setChecked(true);
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    usaSensori = false;
                    new SetSteeringAsynk().execute(0);
                    new SetSpeedAsynk().execute(0);
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    textX.setTextColor(Color.BLACK);
                    textY.setTextColor(Color.BLACK);
                    textZ.setTextColor(Color.BLACK);
                    getView().setBackgroundColor(Color.WHITE);


                    ((ToggleButton) v).setChecked(false);
                }


                return true;

            }
        });

        final Switch switchSensor = (Switch) view.findViewById(R.id.switchSensor);
        switchSensor.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (((Switch) v).isChecked()) {
                                                    usaSensori = true;
                                                } else {
                                                    usaSensori = false;
                                                    new SetSteeringAsynk().execute(0);
                                                    new SetSpeedAsynk().execute(0);
                                                }
                                            }
                                        }
        );


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        Toast.makeText(getActivity(), "Sensori avviati", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        Toast.makeText(getActivity(), "Sensori sosopesi", Toast.LENGTH_SHORT).show();

    }


    float[] oldvalues = {0, 0, 0};
    int rumore = 8;

    @Override
    public void onSensorChanged(SensorEvent event) {
        this.values = event.values;
        for (int i = 0; i < values.length; i++) {
            values[i] = Math.round(values[i] * 10);
        }


        if (usaSensori) {
            int steering = (int) values[1];
            int speed = (int) values[0];



                if (steering > 35) {
                    new SetSteeringAsynk().execute(1);
                }
                if (steering < -35) {
                    new SetSteeringAsynk().execute(-1);
                }
                if (Math.abs(steering) <= 35){
                    new SetSteeringAsynk().execute(0);
                }


            if (Math.abs(speed) >= 25 && (Math.abs(speed - oldvalues[0]) >= rumore)) {

                if (speed > 100) {
                    speed = 100;
                }
                if (speed < -100) {
                    speed = -100;
                }
                new SetSpeedAsynk().execute(speed);
                oldvalues[0] = speed;
            }


            if (Math.abs(speed) < 30 && oldvalues[0] != 0) {
                new SetSpeedAsynk().execute(0);
                oldvalues[0] = 0;
            }

            textX.setText(String.valueOf(Math.round(values[0])));


            textY.setText(String.valueOf(values[1]));
            textZ.setText(String.valueOf(values[2]));

        }
    }

        @Override
        public void onAccuracyChanged (Sensor sensor,int accuracy){

        }
    }
