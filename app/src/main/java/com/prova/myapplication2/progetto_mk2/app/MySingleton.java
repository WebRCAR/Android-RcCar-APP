package com.prova.myapplication2.progetto_mk2.app;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Monica on 22/05/2014.
 */
public class MySingleton {
    private static MySingleton ourInstance;

    public static MySingleton getInstance() {
       if (ourInstance==null){
           ourInstance=new MySingleton();
       }

        return ourInstance;
    }
    public HashMap hashMap = new HashMap();
    ArrayList arrayList =new ArrayList();
    private MySingleton() {

    }

}
