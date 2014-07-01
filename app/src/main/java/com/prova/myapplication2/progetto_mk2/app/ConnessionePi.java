package com.prova.myapplication2.progetto_mk2.app;


import android.view.View;

import com.trouch.webiopi.client.PiClient;
import com.trouch.webiopi.client.PiCoapClient;
import com.trouch.webiopi.client.PiHttpClient;
import com.trouch.webiopi.client.PiMixedClient;
import com.trouch.webiopi.client.devices.digital.NativeGPIO;
import com.trouch.webiopi.client.devices.sensor.Temperature;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by Monica on 22/05/2014.
 */
public class ConnessionePi {

    PiClient client;

    public ConnessionePi(String ipHost) {


        client = new PiMixedClient(ipHost, 8080, PiCoapClient.DEFAULT_PORT);
        client.setCredentials("webiopi", "raspberry");


        //Temperature temp0 = new Temperature(client, "temp0");


    }


    public void setSpeed(float speed) throws Exception {      //VELOCITA'

        client.sendRequest("POST", "/macros/setVelocity/" + speed);

    }

    public void setDirection(int value) throws Exception {      //STERZO
        client.sendRequest("POST", "/macros/setDirection/" + value);

    }


}
