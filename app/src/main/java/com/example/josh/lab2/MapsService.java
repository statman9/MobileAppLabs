package com.example.josh.lab2;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Josh on 10/30/2017.
 */

public class MapsService extends Service {

    final static String ACTION = "NotifyServiceAction";
    final static String STOP_SERVICE_BROADCAST_KEY="StopServiceBroadcastKey";
    final static int RQS_STOP_SERVICE = 1;
    final static List<LatLng> locations = new ArrayList<LatLng>();
    final static float TOTAL_DISTANCE = addLocations();

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
        return distance;
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
}
