package emperatriz.pypots;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.ExecutionException;

import emperatriz.pypots.common.Sys;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS;

public class MessageService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().equals(Sys.DND_KEY)) {
            final String message = new String(messageEvent.getData());


            try{
                    NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.setInterruptionFilter(Integer.parseInt(message));
                    new SendMessage(Sys.DND_KEY, "OK").start();
            }
            catch (SecurityException se){
                new SendMessage(Sys.DND_KEY, "KO").start();
                getApplicationContext().startActivity(new Intent(ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS));
            }
        }
        else if (messageEvent.getPath().equals(Sys.BATTERY_KEY)) {
            Intent batteryIntent = getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int battery = Math.round(level * 100 / (float) scale);

            new SendMessage(Sys.BATTERY_KEY, battery + "").start();
        }
        else if (messageEvent.getPath().equals(Sys.LOCATION_KEY)) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            else{
                try{
                    Location lastlocation = getLastKnownLocation();
                    if (lastlocation!=null){
                        new SendMessage(Sys.LOCATION_KEY, lastlocation.getLatitude()+","+lastlocation.getLongitude()).start();
                    }


                }catch (Exception ex){

                }
            }


        }else if (messageEvent.getPath().equals(Sys.SETINGS_KEY)) {
            final String message = new String(messageEvent.getData());
            for (String set : message.split("#")){
                Sys.save(set.split(",")[0],set.split(",")[1].equals("1"),getApplicationContext());
            }
            Intent intent=new Intent();
            intent.setAction("emperatriz.updateUI");
            sendBroadcast(intent);
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }

    private Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            for (String provider : providers) {
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
        }
        return bestLocation;
    }

    class SendMessage extends Thread {
        String path;
        String message;

        SendMessage(String p, String m) {
            path = p;
            message = m;
        }

        public void run() {
            Task<List<Node>> nodeListTask = Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
            try {
                List<Node> nodes = Tasks.await(nodeListTask);
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask = Wearable.getMessageClient(getApplicationContext()).sendMessage(node.getId(), path, message.getBytes());
                    try {
                        Integer result = Tasks.await(sendMessageTask);
                    } catch (ExecutionException exception) {

                    } catch (InterruptedException exception) {

                    }

                }

            } catch (ExecutionException exception) {

            } catch (InterruptedException exception) {

            }
        }
    }
}
