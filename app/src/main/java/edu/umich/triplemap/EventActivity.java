package edu.umich.triplemap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

public class EventActivity extends AppCompatActivity {

    private boolean editingEvent = false;
    private String previousName;

    private String prevAddress = "";
    private String prevRoom = "";
    private String prevDate = "";
    private String prevStartTime = "";
    private String prevDepartureTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        ((Switch) findViewById(R.id.eventFrequency)).setChecked(intent.getBooleanExtra("eventFrequency", false));

        if(intent.getStringExtra("eventName") != null) {
            editingEvent = true;
            ((EditText) findViewById(R.id.eventName)).setText(intent.getStringExtra("eventName"));
            previousName = intent.getStringExtra("eventName");
        }

        if(intent.getStringExtra("eventAddress") != null) {
            editingEvent = true;
            ((EditText) findViewById(R.id.eventAddress)).setText(intent.getStringExtra("eventAddress"));
            prevAddress = intent.getStringExtra("eventAddress");
        }

        if(intent.getStringExtra("eventRoom") != null) {
            editingEvent = true;
            ((EditText) findViewById(R.id.eventRoom)).setText(intent.getStringExtra("eventRoom"));
            prevRoom = intent.getStringExtra("eventRoom");
        }

        if(intent.getStringExtra("eventDate") != null) {
            editingEvent = true;
            ((EditText) findViewById(R.id.eventDate)).setText(intent.getStringExtra("eventDate"));
            prevDate = intent.getStringExtra("eventDate");
        }

        if(intent.getStringExtra("eventStartTime") != null) {
            editingEvent = true;
            ((EditText) findViewById(R.id.eventStartTime)).setText(intent.getStringExtra("eventStartTime"));
            prevStartTime = intent.getStringExtra("eventStartTime");
        }

        if(intent.getStringExtra("departureTime") != null) {
            prevDepartureTime = intent.getStringExtra("departureTime");
        }

    }

    public void saveEvent(View view) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra("eventFrequency", ((Switch) findViewById(R.id.eventFrequency)).isChecked());
        intent.putExtra("eventName", ((EditText) findViewById(R.id.eventName)).getText().toString());
        intent.putExtra("eventAddress", ((EditText) findViewById(R.id.eventAddress)).getText().toString());
        intent.putExtra("eventRoom", ((EditText) findViewById(R.id.eventRoom)).getText().toString());
        intent.putExtra("eventDate", ((EditText) findViewById(R.id.eventDate)).getText().toString());
        intent.putExtra("eventStartTime", ((EditText) findViewById(R.id.eventStartTime)).getText().toString());
        intent.putExtra("editingEvent", editingEvent);
        intent.putExtra("previousName", previousName);

        if(prevAddress.equals(((EditText) findViewById(R.id.eventAddress)).getText().toString())
                && prevRoom.equals(((EditText) findViewById(R.id.eventRoom)).getText().toString())
                && prevDate.equals(((EditText) findViewById(R.id.eventDate)).getText().toString())
                && prevStartTime.equals(((EditText) findViewById(R.id.eventStartTime)).getText().toString())
                && prevDepartureTime != null) {
            intent.putExtra("departureTime", prevDepartureTime);
        }

        startActivity(intent);
        finish();
    }

    public void cancelChanges(View view) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra("cancelRequest", true);
        startActivity(intent);
        finish();
    }

    public void deleteEvent(View view) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra("previousName", previousName);
        intent.putExtra("deleteRequest", true);
        intent.putExtra("eventName", "placeholderForNotNull");
        startActivity(intent);
        finish();
    }
}
