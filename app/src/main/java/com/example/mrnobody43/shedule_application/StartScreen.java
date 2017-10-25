package com.example.mrnobody43.shedule_application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import Utils.Parser;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        Parser parser = new Parser();

        parser.execute();
    }
}
