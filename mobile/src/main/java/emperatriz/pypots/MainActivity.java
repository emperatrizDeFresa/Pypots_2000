package emperatriz.pypots;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.ExecutionException;

import emperatriz.pypots.common.Sys;

public class MainActivity extends Activity {


    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            updated=true;
            updateUI();
        }
    }

    boolean updated =false;
    TextView info;


    public void updateUI(){
        final CheckBox cbNumeroNotificaciones = (CheckBox) findViewById(R.id.cbNumeroNotificaciones);
        final CheckBox cbNotificacionesNoLeidas = (CheckBox) findViewById(R.id.cbNotificacionesNoLeidas);
        final CheckBox cbPasos = (CheckBox) findViewById(R.id.cbPasos);

        cbNumeroNotificaciones.setChecked(Sys.getBoolean(Sys.SETTINGS_NUMERO_NOTIFICACIONES, false, MainActivity.this));
        cbNotificacionesNoLeidas.setChecked(Sys.getBoolean(Sys.SETTINGS_NOTIFICACIONES_NO_LEIDAS, false, MainActivity.this));
        cbPasos.setChecked(Sys.getBoolean(Sys.SETTINGS_PASOS, false, MainActivity.this));

        cbNumeroNotificaciones.setEnabled(true);
        cbNotificacionesNoLeidas.setEnabled(true);
        cbPasos.setEnabled(true);

        info.animate().translationY(info.getHeight()).alpha(0.f).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // superfluous restoration
                info.setVisibility(View.GONE);
                info.setAlpha(1.f);
                info.setTranslationY(0.f);
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        IntentFilter filter = new IntentFilter("emperatriz.updateUI");
        this.registerReceiver(new Receiver(), filter);

        new SendMessage(Sys.SETINGS_KEY, "").start();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        }


        final CheckBox cbNumeroNotificaciones = (CheckBox) findViewById(R.id.cbNumeroNotificaciones);
        final CheckBox cbNotificacionesNoLeidas = (CheckBox) findViewById(R.id.cbNotificacionesNoLeidas);
        final CheckBox cbPasos = (CheckBox) findViewById(R.id.cbPasos);

        cbNumeroNotificaciones.setChecked(Sys.getBoolean(Sys.SETTINGS_NUMERO_NOTIFICACIONES, false, MainActivity.this));
        cbNotificacionesNoLeidas.setChecked(Sys.getBoolean(Sys.SETTINGS_NOTIFICACIONES_NO_LEIDAS, false, MainActivity.this));
        cbPasos.setChecked(Sys.getBoolean(Sys.SETTINGS_PASOS, false, MainActivity.this));

        cbNumeroNotificaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sys.save(Sys.SETTINGS_NUMERO_NOTIFICACIONES, isChecked, MainActivity.this);
                new SendMessage(Sys.SETINGS_UP_KEY, Sys.SETTINGS_NUMERO_NOTIFICACIONES+","+(isChecked?"1":"0")).start();
            }
        });

        cbNotificacionesNoLeidas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sys.save(Sys.SETTINGS_NOTIFICACIONES_NO_LEIDAS, isChecked, MainActivity.this);
                new SendMessage(Sys.SETINGS_UP_KEY, Sys.SETTINGS_NOTIFICACIONES_NO_LEIDAS+","+(isChecked?"1":"0")).start();
            }
        });

        cbPasos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sys.save(Sys.SETTINGS_PASOS, isChecked, MainActivity.this);
                new SendMessage(Sys.SETINGS_UP_KEY, Sys.SETTINGS_PASOS+","+(isChecked?"1":"0")).start();
            }
        });

        cbNumeroNotificaciones.setEnabled(false);
        cbNotificacionesNoLeidas.setEnabled(false);
        cbPasos.setEnabled(false);

        info = findViewById(R.id.info);

        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!updated){
                    cbNumeroNotificaciones.setEnabled(true);
                    cbNotificacionesNoLeidas.setEnabled(true);
                    cbPasos.setEnabled(true);
                    info.setText(getResources().getString(R.string.no_reloj));
                    info.setBackgroundColor(0xffff3344);
                }

            }
        };

        handler.postDelayed(runnable, 5000);

        final CanvasView cv = findViewById(R.id.canvasView);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv.incrementNotification();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(MainActivity.this, getResources().getString(R.string.no_gps), Toast.LENGTH_SHORT).show();
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