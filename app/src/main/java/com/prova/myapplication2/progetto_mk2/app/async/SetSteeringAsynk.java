package com.prova.myapplication2.progetto_mk2.app.async;

import android.os.AsyncTask;
import android.util.Log;

import com.prova.myapplication2.progetto_mk2.app.ConnessionePi;
import com.prova.myapplication2.progetto_mk2.app.MySingleton;

/**
 * Created by Monica on 05/06/2014.
 */
public class SetSteeringAsynk extends AsyncTask<Integer, Integer, Integer> {


    @Override
    protected Integer doInBackground(Integer[] params) {
        MySingleton singleton = MySingleton.getInstance();

        Log.w("@@@SET STEERING@@@", "Eseguito: params[0] =  # "+params[0]+" #");
        try {
            ConnessionePi connessionePi = (ConnessionePi) singleton.hashMap.get("CONN");
            connessionePi.setDirection(Math.round(params[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }
}
