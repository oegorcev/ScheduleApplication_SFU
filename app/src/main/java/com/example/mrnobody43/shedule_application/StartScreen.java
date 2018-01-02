package com.example.mrnobody43.shedule_application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import Utils.Constants;
import Utils.Scheduler;
import model.Week;

public class StartScreen extends AppCompatActivity {

    private Week _currentSchedule;
    private ListView listView1;
    private ListView listView2;
    private ListView listView3;
    private ListView listView4;
    private ListView listView5;
    private ListView listView6;
    private ListView listView7;

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        setTitle("КТбо4-8");

        Scheduler scheduler = new Scheduler("КТбо4-8");
        scheduler.execute(_currentSchedule);

        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");

        tabSpec.setContent(R.id.list1);
        tabSpec.setIndicator(Constants.MONDAY);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.list2);
        tabSpec.setIndicator(Constants.TUESDAY);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.list3);
        tabSpec.setIndicator(Constants.WEDNESDAY);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setContent(R.id.list4);
        tabSpec.setIndicator(Constants.THURSDAY);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag5");
        tabSpec.setContent(R.id.list5);
        tabSpec.setIndicator(Constants.FRIDAY);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag6");
        tabSpec.setContent(R.id.list6);
        tabSpec.setIndicator(Constants.SATURDAY);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag7");
        tabSpec.setContent(R.id.list7);
        tabSpec.setIndicator(Constants.SUNDAY);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);

        String[] names = { "Иван", "Марья", "Петр", "Антон", "Даша", "Борис",
                "Костя", "Игорь", "Анна", "Денис", "Андрей" };

        /** Called when the activity is first created. */
        // находим список
        listView1 = (ListView) findViewById(R.id.list1);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, names);
        listView1.setAdapter(adapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
