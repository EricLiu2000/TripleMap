package edu.umich.triplemap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private HashMap<String, String[]> events;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            events = ScheduleActivity.getEvents();
            //TODO: more update stuff here
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("changedEvents"));
        setContentView(R.layout.activity_main);
    }

    public void goToSchedule(View view) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
    }
}
