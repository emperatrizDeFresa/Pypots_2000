package emperatriz.pypots;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.ExecutionException;

import emperatriz.pypots.common.Sys;

import static android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS;

public class MessageService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(Sys.DND_KEY)) {
            final String message = new String(messageEvent.getData());
        }
        else if (messageEvent.getPath().equals(Sys.BATTERY_KEY)) {
            final String message = new String(messageEvent.getData());
            int value = Integer.parseInt(message);
            Sys.save(Sys.BATTERY_KEY,value,this);
        }
        else if (messageEvent.getPath().equals(Sys.LOCATION_KEY)) {
            final String message = new String(messageEvent.getData());
            Sys.save(Sys.LOCATION_KEY,message,this);
        }
        else if (messageEvent.getPath().equals(Sys.SETINGS_KEY)) {
            String ret = Sys.SETTINGS_NUMERO_NOTIFICACIONES+","+(Sys.getBoolean(Sys.SETTINGS_NUMERO_NOTIFICACIONES,false, getApplicationContext())?"1":"0")+"#"+
                        Sys.SETTINGS_NOTIFICACIONES_NO_LEIDAS+","+(Sys.getBoolean(Sys.SETTINGS_NOTIFICACIONES_NO_LEIDAS,false, getApplicationContext())?"1":"0")+"#"+
                        Sys.SETTINGS_PASOS+","+(Sys.getBoolean(Sys.SETTINGS_PASOS,false, getApplicationContext())?"1":"0");
            new SendMessage(Sys.SETINGS_KEY, ret).start();
        }
        else if (messageEvent.getPath().equals(Sys.SETINGS_UP_KEY)) {
            final String message = new String(messageEvent.getData());
            if (message.split(",")[0].equals((Sys.SETTINGS_DIVISIONES))){
                Sys.save(message.split(",")[0],Integer.parseInt(message.split(",")[1]),getApplicationContext());
            }
            else{
                Sys.save(message.split(",")[0],message.split(",")[1].equals("1"),getApplicationContext());
            }
        }
        else {
            super.onMessageReceived(messageEvent);
        }
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
