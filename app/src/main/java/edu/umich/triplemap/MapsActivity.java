package edu.umich.triplemap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private GoogleMap mMap;

    private LocationManager locationManager;

    private EventList events;

    private void broadcastChanges() {
        Intent changedIntent = new Intent("updatedRoutes");
        LocalBroadcastManager.getInstance(this).sendBroadcast(changedIntent);
    }

    public void backToSchedule(View view) {
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3).setApiKey(getString(R.string.google_maps_key))
                .setConnectTimeout(1, TimeUnit.SECONDS).setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    private boolean checkEventDetails(Event event) {
        if(event.getName() == null || event.getName().equals("")) {
            return false;
        } else if(event.getAddress() == null || event.getAddress().equals("")) {
            return false;
        } else if(event.getStartTime() == null || event.getStartTime().equals("")) {
            return false;
        } else if(event.getDate() == null || event.getDate().equals("")) {
            return false;
        }
        return true;
    }

    private int getNumbersFromString(String s) {
        String nums = "";
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == '0' || s.charAt(i) == '1' || s.charAt(i) == '2' || s.charAt(i) == '3' || s.charAt(i) == '4'
                    || s.charAt(i) == '5' || s.charAt(i) == '6' || s.charAt(i) == '7' || s.charAt(i) == '8' || s.charAt(i) == '9') {
                nums += s.charAt(i);
            }
        }
        if(nums.length() == 0) {
            return -1;
        } else {
            return Integer.valueOf(nums);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        events = ScheduleActivity.getEvents();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        Double lat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        Double lon = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();

        DateTime time = new DateTime();

        for(int i = 0; i < events.size(); i++) {

            long timeInSeconds = 0;

            if(checkEventDetails(events.get(i))) {
                try {
                    DirectionsResult result = DirectionsApi.newRequest(getGeoContext()).mode(TravelMode.TRANSIT)
                            .origin(new com.google.maps.model.LatLng(lat, lon)).destination(((Event) events.get(i)).getAddress()).departureTime(time).await();
                    for(int j = 0; j < result.routes[0].legs.length; j++) {
                        timeInSeconds += result.routes[0].legs[j].duration.inSeconds;
                    }

                    if(!events.get(i).getRoom().equals("") && events.get(i).getRoom() != null) {
                        int floorNumber = getNumbersFromString(events.get(i).getRoom());

                        if(floorNumber >= 3000) {
                        // Likely taking the elevator
                            // Doors opening and closing
                            timeInSeconds += 10;
                            // 2 seconds per floor
                            timeInSeconds += (Math.floor(floorNumber / 1000.0) - 1) * 2.0;
                            // Within floor transit
                            timeInSeconds += 45;
                        } else if(floorNumber >= 2000) {
                        // Likely 1 or 2 flights of stairs
                            // 9 seconds per floor
                            timeInSeconds += (Math.floor(floorNumber / 1000.0) - 1) * 9.0;
                            // Within floor transit
                            timeInSeconds += 45;
                        } else {
                        // Ground floor
                            // Within floor transit
                            timeInSeconds += 45;
                        }
                    }

                    events.get(i).setLengthInSeconds(timeInSeconds);

                    PolylineOptions options  = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                    List<LatLng> list = new ArrayList<>();
                    for(com.google.maps.model.LatLng pos : result.routes[0].overviewPolyline.decodePath()) {
                        list.add(new LatLng(pos.lat, pos.lng));
                    }
                    mMap.addPolyline(options.addAll(list));

                    broadcastChanges();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    try{
                        mMap.setMyLocationEnabled(true);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    // Hope it doesn't get here because then we're dead
                }
                break;
            }
        }
    }
}
