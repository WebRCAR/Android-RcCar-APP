package com.prova.myapplication2.progetto_mk2.app;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.URI;

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
    private boolean suspending = false;
    private MjpegView mv;
    String URL;
    private int width = 640;
    private int height = 480;
    private String ip_command = "?action=stream";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mv = new MjpegView(getActivity());



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

        URL= (String)"http://"+MySingleton.getInstance().hashMap.get("IP")+":8081/?action=stream";

        mv = (MjpegView) view.findViewById(R.id.mv);
        if(mv != null){
            mv.setResolution(width, height);
        }

       new DoRead().execute(URL);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        Toast.makeText(getActivity(), "Sensori avviati", Toast.LENGTH_SHORT).show();
        if(mv!=null){
            if(suspending){
                new DoRead().execute(URL);
                suspending = false;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        Toast.makeText(getActivity(), "Sensori sosopesi", Toast.LENGTH_SHORT).show();
        if(mv!=null){
            if(mv.isStreaming()){
                mv.stopPlayback();
                suspending = true;
            }
        }

    }

    boolean DEBUG =true;
    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground(String... url) {

            //TODO: if camera has authentication deal with it and don't just not work
            HttpResponse res = null;
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpParams httpParams = httpclient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5 * 1000);
            HttpConnectionParams.setSoTimeout(httpParams, 5*1000);
            if(DEBUG) Log.d(" ", "1. Sending http request");
            try {
                res = httpclient.execute(new HttpGet(URI.create(url[0])));
                if(DEBUG) Log.d(" ", "2. Request finished, status = " + res.getStatusLine().getStatusCode());
                if(res.getStatusLine().getStatusCode()==401){
                    //You must turn off camera User Access Control before this will work
                    return null;
                }
                return new MjpegInputStream(res.getEntity().getContent());
            } catch (ClientProtocolException e) {
                if(DEBUG){
                    e.printStackTrace();
                    Log.d(" ", "Request failed-ClientProtocolException", e);
                }
                //Error connecting to camera
            } catch (IOException e) {
                if(DEBUG){
                    e.printStackTrace();
                    Log.d(" ", "Request failed-IOException", e);
                }
                //Error connecting to camera
            } catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(MjpegInputStream result) {
            mv.setSource(result);
            if(result!=null){
                result.setSkip(1);

            }else{

            }
            mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
            mv.showFps(false);
        }
    }
/*
    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground(String... url) {
            //TODO: if camera has authentication deal with it and don't just not work
            HttpResponse res = null;
            DefaultHttpClient httpclient = new DefaultHttpClient();
            Log.d(" ", "1. Sending http request");
            try {
                res = httpclient.execute(new HttpGet(URI.create(url[0])));
                Log.d(" ", "2. Request finished, status = " + res.getStatusLine().getStatusCode());
                if(res.getStatusLine().getStatusCode()==401){
                    //You must turn off camera User Access Control before this will work
                    return null;
                }
                return new MjpegInputStream(res.getEntity().getContent());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                Log.d(" ", "Request failed-ClientProtocolException", e);
                //Error connecting to camera
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(" ", "Request failed-IOException", e);
                //Error connecting to camera
            }
            return null;
        }

        protected void onPostExecute(MjpegInputStream result) {
            mv.setSource(result);
            mv.setDisplayMode(MjpegView.SIZE_STANDARD);   //SIZE_BEST_FIT
            mv.showFps(true);
        }
    }


*/

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
