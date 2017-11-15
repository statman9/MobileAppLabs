package com.example.josh.lab2;

import android.app.Service;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.content.Intent;
import java.math.BigDecimal;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Josh on 10/30/2017.
 */

public class MapsService extends Service {

    public LocationManager locationManager;
    public MyLocationListener listener;
    final static String ACTION = "NotifyServiceAction";
    final static String STOP_SERVICE_BROADCAST_KEY="StopServiceBroadcastKey";
    final static int RQS_STOP_SERVICE = 1;
    public Location previousLocation;
    final static List<LatLng> locations = new ArrayList<>();
    final static List<Date> times = new ArrayList<>();
    Intent intent;

    @Override
    public IBinder onBind(Intent intent) {
        // Implement your logic here.
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
        addLocations();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    static float addLocations() {
        float distance = 0;
        if (locations.size() > 1) {
            for (int i = 0; i < locations.size(); i++) {
                float distanceAtoB =0;
                if (i > 0) {
                    Location location = new Location("");
                    location.setLatitude(locations.get(i-1).latitude);
                    location.setLongitude(locations.get(i-1).longitude);

                    Location location1 = new Location("");
                    location1.setLatitude(locations.get(i).latitude);
                    location1.setLongitude(locations.get(i).longitude);

                    distanceAtoB = location.distanceTo(location1);
                    distance += distanceAtoB;
                }
            }
        }
        float milesRatio = (float)0.621371;
        distance = (distance / 1000) * milesRatio;
        BigDecimal bd = new BigDecimal(Float.toString(distance));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        distance = bd.floatValue();
        return distance;
    }

    static float addTimes() {
        float time = 0;
        if (times.size() > 1) {
            for (int i = 0; i < times.size(); i++) {
                float timeAtoB = 0;
                if (i > 0) {
                    timeAtoB = ((float)(times.get(i).getTime()-times.get(i-1).getTime()));
                    timeAtoB = (timeAtoB / (1000*60)) % 60;
                }
                time += timeAtoB;
            }
        }
        return time;
    }

    public class NotifyServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            int rqs = arg1.getIntExtra(STOP_SERVICE_BROADCAST_KEY, 0);

            if (rqs == RQS_STOP_SERVICE){
                stopSelf();
            }
        }
    }

    public class MyLocationListener implements LocationListener
    {
        public void onLocationChanged(final Location loc)
        {
            LatLng latlng=fromLocationToLatLng(loc);
            locations.add(latlng);
            Date now = new Date();
            times.add(now);
                intent.putExtra("Distance", addLocations());
                intent.putExtra("Time", addTimes());
                sendBroadcast(intent);
        }

        public LatLng fromLocationToLatLng(Location location){
            return new LatLng(location.getLatitude(), location.getLongitude());

        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }

        public void onStatusChanged(String provider, int status, Bundle bundle)
        {
            return;
        }
    }
}
