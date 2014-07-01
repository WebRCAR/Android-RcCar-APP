package com.prova.myapplication2.progetto_mk2.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;


public class mainActivity extends ActionBarActivity {

    Button button1,button2Connetti;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button) findViewById(R.id.button1);
        button2Connetti = (Button) findViewById(R.id.button2);
        MySingleton mySingleton = MySingleton.getInstance();



        if (mySingleton.hashMap.get("IP")==null){
            button1.setEnabled(false);
        }

        button1.setOnClickListener(listener);
        button2Connetti.setOnClickListener(listener);

        MySingleton singleton = MySingleton.getInstance();

        /*
        connessione

         */
    }

    View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.button1:
                    // GUIDA!
                    Intent intent   =   new Intent(v.getContext(), DriveActivity.class);

                    startActivity(intent);
                    break;
                case R.id.button2:
                    //SETTINGS
                    Intent intent2   =   new Intent(v.getContext(), SettingsActivity.class);
                    startActivity(intent2);
                   break;
            }
        }
    };

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
               // Intent intent = new Intent(this, SensorActivity.class);
                //this.startActivity(intent);
        }
    }
}
