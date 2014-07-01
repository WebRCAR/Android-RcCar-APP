package com.prova.myapplication2.progetto_mk2.app.async;

import android.os.AsyncTask;

import com.prova.myapplication2.progetto_mk2.app.ConnessionePi;
import com.prova.myapplication2.progetto_mk2.app.MySingleton;

/**
 * Created by Monica on 03/06/2014.
 */
public class ConnectTask extends AsyncTask {




    ConnessionePi connessionePi;

    @Override
    protected Object doInBackground(Object[] params) {
        MySingleton singleton = MySingleton.getInstance();
        ConnessionePi connessionePi = new ConnessionePi((String) singleton.hashMap.get("IP"));
        singleton.hashMap.put("CONN",connessionePi);

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

    }

}
