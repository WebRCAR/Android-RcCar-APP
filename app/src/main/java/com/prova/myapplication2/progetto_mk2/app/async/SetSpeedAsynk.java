package com.prova.myapplication2.progetto_mk2.app.async;

import android.os.AsyncTask;
import android.util.Log;

import com.prova.myapplication2.progetto_mk2.app.ConnessionePi;
import com.prova.myapplication2.progetto_mk2.app.MySingleton;

/**
 * Created by Monica on 05/06/2014.
 */
public class SetSpeedAsynk extends AsyncTask<Integer, Integer, Integer> {


    @Override
    protected Integer doInBackground(Integer[] params) {
        Log.w("@@@SET SPEED@@@", "Eseguito: params[0] =  # "+params[0]+" #");
        MySingleton singleton = MySingleton.getInstance();
        try {
            ConnessionePi connessionePi = (ConnessionePi) singleton.hashMap.get("CONN");
            connessionePi.setSpeed((float)Math.round(params[0])/100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


}
