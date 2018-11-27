package edu.umich.triplemap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ScheduleActivity extends AppCompatActivity {

    private HashMap<String, String[]> events = new HashMap<String, String[]>();

    public HashMap<String, String[]> getEvents() {
        return events;
    }

    Spinner spinner;
    ArrayAdapter<String> dataAdapter;

    private void recordEvent() {
        Intent intent = getIntent();

        String[] details = new String[6];
        details[0] = intent.getStringExtra("eventFrequency");
        details[1] = intent.getStringExtra("eventName");
        details[2] = intent.getStringExtra("eventAddress");
        details[3] = intent.getStringExtra("eventRoom");
        details[4] = intent.getStringExtra("eventDate");
        details[5] = intent.getStringExtra("eventStartTime");

        events.put(intent.getStringExtra("eventName"), details);
    }

    public void createNewEvent(View view) {
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        ArrayList<String> initialMessage = new ArrayList();
        initialMessage.add("Please create an event");
        spinner  = findViewById(R.id.spinner2);
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, initialMessage);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setContentView(R.layout.activity_schedule);
        setIntent(intent);
        recordEvent();

        spinner = findViewById(R.id.spinner2);
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(events.keySet()));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
}