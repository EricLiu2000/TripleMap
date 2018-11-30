package edu.umich.triplemap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {

    private static EventList events = new EventList();

    public static EventList getEvents() {
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

        if(intent.getStringExtra("eventName") == null) {
            return;
        }

        String[] details = new String[5];
        details[0] = intent.getStringExtra("eventName");
        details[1] = intent.getStringExtra("eventAddress");
        details[2] = intent.getStringExtra("eventRoom");
        details[3] = intent.getStringExtra("eventDate");
        details[4] = intent.getStringExtra("eventStartTime");

        Event event = new Event();
        event.setIsWeekly(intent.getBooleanExtra("eventFrequency", false));
        event.setName(details[0]);
        event.setAddress(details[1]);
        event.setRoom(details[2]);
        event.setDate(details[3]);
        event.setStartTime(details[4]);

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
            events.add(event);
        }
    }

    public void createNewEvent(View view) {
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }

    public void editEvent(View view) {
        Intent intent = new Intent(this, EventActivity.class);
        Event event = (Event) events.get(((Spinner) findViewById(R.id.spinner2)).getSelectedItem().toString());
        intent.putExtra("eventFrequency", event.getIsWeekly());
        intent.putExtra("eventName", event.getName());
        intent.putExtra("eventAddress", event.getAddress());
        intent.putExtra("eventRoom", event.getRoom());
        intent.putExtra("eventDate", event.getDate());
        intent.putExtra("eventStartTime", event.getStartTime());
        startActivity(intent);
    }

    public void deleteEvent(View view) {
        broadcastChanges();
        Spinner spinner = findViewById(R.id.spinner2);
        events.remove(spinner.getSelectedItem().toString());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, events);
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, events);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        checkButtonSafety();
    }
}