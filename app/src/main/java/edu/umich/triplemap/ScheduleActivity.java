package edu.umich.triplemap;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleActivity extends AppCompatActivity {

    private static HashMap<String, String[]> events = new HashMap<>();

    public static HashMap<String, String[]> getEvents() {
        return events;
    }

    private void checkButtonSafety() {
        if(events.size() == 0) {
            findViewById(R.id.editEvent).setClickable(false);
            findViewById(R.id.deleteEventFromSchedule).setClickable(false);
        } else {
            findViewById(R.id.editEvent).setClickable(true);
            findViewById(R.id.deleteEventFromSchedule).setClickable(true);
        }
    }

    private void broadcastChanges() {
        // Broadcast that the events hashmap has changed, so main will know
        Intent changedIntent = new Intent("changedEvents");
        LocalBroadcastManager.getInstance(this).sendBroadcast(changedIntent);
    }

    private void recordEvent() {
        Intent intent = getIntent();

        String[] details = new String[6];
        details[0] = Boolean.toString(intent.getBooleanExtra("eventFrequency", false));
        details[1] = intent.getStringExtra("eventName");
        details[2] = intent.getStringExtra("eventAddress");
        details[3] = intent.getStringExtra("eventRoom");
        details[4] = intent.getStringExtra("eventDate");
        details[5] = intent.getStringExtra("eventStartTime");

        boolean editingEvent = intent.getBooleanExtra("editingEvent", false);

        //Deleting event
        if(intent.getBooleanExtra("deleteRequest", false)) {
            broadcastChanges();

            // Remove the desired event from the events hashmap
            events.remove(intent.getStringExtra("previousName"));
        } else if(intent.getBooleanExtra("cancelRequest", false)) {
        //do nothing on cancel
        } else {
        // Either editing or creating an event

            broadcastChanges();

            if(editingEvent) {
                events.remove(intent.getStringExtra("previousName"));
            }
            events.put(intent.getStringExtra("eventName"), details);
        }
    }

    public void createNewEvent(View view) {
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }

    public void editEvent(View view) {
        Intent intent = new Intent(this, EventActivity.class);
        String[] details = events.get(((Spinner) findViewById(R.id.spinner2)).getSelectedItem().toString());
        intent.putExtra("eventFrequency", details[0]);
        intent.putExtra("eventName", details[1]);
        intent.putExtra("eventAddress", details[2]);
        intent.putExtra("eventRoom", details[3]);
        intent.putExtra("eventDate", details[4]);
        intent.putExtra("eventStartTime", details[5]);
        startActivity(intent);
    }

    public void deleteEvent(View view) {
        broadcastChanges();
        Spinner spinner = findViewById(R.id.spinner2);
        events.remove(spinner.getSelectedItem().toString());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(events.keySet()));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        checkButtonSafety();
    }

    public void goToMaps(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        ArrayList<String> initialMessage = new ArrayList();
        initialMessage.add("Please create an event");
        Spinner spinner  = findViewById(R.id.spinner2);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, initialMessage);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        checkButtonSafety();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setContentView(R.layout.activity_schedule);
        setIntent(intent);
        recordEvent();

        Spinner spinner = findViewById(R.id.spinner2);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(events.keySet()));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        checkButtonSafety();
    }
}