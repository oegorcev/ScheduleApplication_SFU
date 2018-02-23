package com.example.mrnobody43.shedule_application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import Utils.Constants;

/**
 * Created by Mr.Nobody43 on 24.01.2018.
 */

public class ChangeSchedule extends AppCompatActivity {
    Button changeSchedule;
    EditText search;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_schedule);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changeSchedule = (Button) findViewById(R.id.changeSchedule);
        search = (EditText) findViewById(R.id.search);


        changeSchedule.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent intent = new Intent();

                intent.putExtra(Constants.CHANGED_SCHEDULE, search.getText().toString());

                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
