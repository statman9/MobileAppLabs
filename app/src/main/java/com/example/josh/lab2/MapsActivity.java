package com.example.josh.lab2;

import android.content.Intent;
import android.app.Fragment;
import android.view.View;
import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public Marker whereAmI;
    PolylineOptions rectOptions = new PolylineOptions();
    Polyline polyline;
    private CommentsDataSource datasource = new CommentsDataSource(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        LocationManager locationManager;
        String svcName= Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getSystemService(svcName);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        String provider = locationManager.getBestProvider(criteria, true);

        Intent prevIntent = getIntent();

        if (prevIntent.getStringExtra(MainActivity.INPUT_LATITUDES) != null) {
            Button myButton = (Button)findViewById(R.id.saveButton);
            myButton.setEnabled(false);
            List<Location> locations = new ArrayList<>();
            String[] latitudes = prevIntent.getStringExtra(MainActivity.INPUT_LATITUDES).split(",");
            String[] longitudes = prevIntent.getStringExtra(MainActivity.INPUT_LATITUDES).split(",");
            for (int i = 0; i < latitudes.length; i++) {
                Location l = new Location("");
                Double latitude = Double.parseDouble(latitudes[i]);
                Double longitude = Double.parseDouble(longitudes[i]);
                l.setLatitude(latitude);
                l.setLongitude(longitude);
                locations.add(l);
            }
            for (int i = 0; i < locations.size(); i++) {
                updateWithNewLocation(locations.get(i));
            }
        }
        else {
            Intent intent = new Intent(this, MapsService.class);
            MapsActivity.this.startService(intent);

            Location l = locationManager.getLastKnownLocation(provider);

            LatLng latlng = fromLocationToLatLng(l);

            MapsService.locations.add(latlng);
            MapsService.times.add(new Date());


            whereAmI = mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_GREEN)));
            // Zoom in
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,
                    17));

            updateWithNewLocation(l);

            locationManager.requestLocationUpdates(provider, 2000, 10,
                    locationListener);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        Intent intent = new Intent(this, MapsService.class);
        MapsActivity.this.startService(intent);
        MapsService.times.add(new Date());
    }

    public static LatLng fromLocationToLatLng(Location location){
        return new LatLng(location.getLatitude(), location.getLongitude());

    }

    public void saveData(View v) {
        Intent intent = getIntent();
        datasource = new CommentsDataSource(this);
        Date datetime = new Date();
        MapsService.times.add(datetime);
        long dateTime = datetime.getTime();
        String inputType = intent.getStringExtra(MainActivity.INPUT_TYPE);
        String activityType = intent.getStringExtra(MainActivity.INPUT_ACTIVITY);
        String longitudes = "";
        String latitudes = "";
        for (int i = 1; i < MapsService.locations.size(); i++) {
            if (i != 1) {
                longitudes += "," + MapsService.locations.get(i).longitude;
                latitudes += "," + MapsService.locations.get(i).latitude;
            }
            else {
                longitudes += MapsService.locations.get(i).longitude;
                latitudes += MapsService.locations.get(i).latitude;
            }
        }
        datasource.open();
        datasource.createComment(inputType, activityType, dateTime, MapsService.addTimes(), (int)MapsService.addLocations(), 0, 0, "", latitudes, longitudes);
        datasource.close();
        Intent intent2 = new Intent();
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
        intent2.setAction(MapsService.ACTION);
        intent2.putExtra(MapsService.STOP_SERVICE_BROADCAST_KEY, MapsService.RQS_STOP_SERVICE);
        sendBroadcast(intent2);

    }

    private void updateWithNewLocation(Location location) {
        TextView myLocationText;
        myLocationText = (TextView)findViewById(R.id.myLocationText);

        String latLongString = "No location found";

        Intent intent = new Intent(this, MapsService.class);
        MapsActivity.this.startService(intent);

        if (location != null) {
            // Update the map location.

            LatLng latlng=fromLocationToLatLng(location);
            MapsService.locations.add(latlng);

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,
                    17));

            if(whereAmI!=null) {
                if(polyline != null){
                    polyline.remove();
                    polyline = null;
                }
                    rectOptions.add(whereAmI.getPosition());
                    rectOptions.color(Color.RED);
                    polyline = mMap.addPolyline(rectOptions);

                whereAmI.remove();
            }

            whereAmI=mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_GREEN)));

            latLongString = "Distance:" + MapsService.addLocations() + "\nTime:" + MapsService.addTimes();
        }

        myLocationText.setText(latLongString);
    }



    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {}
    };

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            map.getMapAsync(this);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
                // Configure the map display options

            }
        }
    }
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }
}
