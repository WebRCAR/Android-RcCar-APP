package com.prova.myapplication2.progetto_mk2.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import static android.support.v4.app.ActivityCompat.startActivity;
import static android.view.View.*;


public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        Button button1 = (Button) findViewById(R.id.salvaIPbtn);

        final Switch switchIP = (Switch) findViewById(R.id.switchIP);

        final EditText domain = (EditText) findViewById(R.id.editTextDomain);
        domain.setEnabled(false);
        final LinearLayout ipLay = (LinearLayout) findViewById(R.id.LinearLayIP);
        final EditText editText1 = (EditText) findViewById(R.id.editTextIP1);
        final EditText editText2 = (EditText) findViewById(R.id.editTextIP2);
        final EditText editText3 = (EditText) findViewById(R.id.editTextIP3);
        final EditText editText4 = (EditText) findViewById(R.id.editTextIP4);

        OnClickListener listener = new OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.salvaIPbtn:
                        Intent intent = new Intent(v.getContext(), mainActivity.class);
                        MySingleton m = MySingleton.getInstance();
                        String ip = "";

                        if (switchIP.isChecked()) {
                            ip= domain.getText().toString();
                        }else {
                            ip = (editText1.getText() + "." + editText2.getText() + "." + editText3.getText() + "." + editText4.getText());
                        }
                        Toast toast = Toast.makeText(getApplicationContext(), "IP: " + ip, Toast.LENGTH_LONG);
                        toast.show();
                        m.hashMap.put("IP", ip);
                        //m.hashMap.get("IP");
                        startActivity(intent);
                    case R.id.switchIP:
                        if (switchIP.isChecked()) {
                            for (int i = 0; i < ipLay.getChildCount(); i++) {
                                View view = ipLay.getChildAt(i);
                                view.setEnabled(false);
                            }

                            domain.setEnabled(true);
                        } else {
                            for (int i = 0; i < ipLay.getChildCount(); i++) {
                                View view = ipLay.getChildAt(i);
                                view.setEnabled(true);
                            }

                            domain.setEnabled(false);
                        }
                }
            }
        };

        button1.setOnClickListener(listener);
        switchIP.setOnClickListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.connect, menu);
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


}

