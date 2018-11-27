package edu.umich.triplemap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
    }

    public void saveEvent(View view) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        intent.putExtra("eventFrequency", ((Switch) findViewById(R.id.eventFrequency)).isChecked());
        intent.putExtra("eventName", ((EditText) findViewById(R.id.eventName)).getText().toString());
        intent.putExtra("eventAddress", ((EditText) findViewById(R.id.eventAddress)).getText().toString());
        intent.putExtra("eventRoom", ((EditText) findViewById(R.id.eventRoom)).getText().toString());
        intent.putExtra("eventDate", ((EditText) findViewById(R.id.eventDate)).getText().toString());
        intent.putExtra("eventStartTime", ((EditText) findViewById(R.id.eventStartTime)).getText().toString());
        startActivity(intent);
    }

    public void cancelChanges(View view) {

    }

    public void deleteEvent(View view) {

    }
}
