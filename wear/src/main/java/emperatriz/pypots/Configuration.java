package emperatriz.pypots;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import emperatriz.pypots.common.Sys;

import static android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS;

public class Configuration extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        CheckBox cbNumeroNotificaciones = (CheckBox)findViewById(R.id.cbNumeroNotificaciones);
        CheckBox cbNotificacionesNoLeidas = (CheckBox)findViewById(R.id.cbNotificacionesNoLeidas);
        //CheckBox cbPasos = (CheckBox)findViewById(R.id.cbPasos);
        CheckBox cbTorch = (CheckBox)findViewById(R.id.cbTorch);
        CheckBox cbDND = (CheckBox)findViewById(R.id.cbDND);
        CheckBox cbHalo = (CheckBox)findViewById(R.id.cbHalo);
//        Spinner divisiones = (Spinner)findViewById(R.id.divisiones);

        cbNumeroNotificaciones.setChecked(Sys.getBoolean(Sys.SETTINGS_NUMERO_NOTIFICACIONES,false, Configuration.this));
        cbNotificacionesNoLeidas.setChecked(Sys.getBoolean(Sys.SETTINGS_NOTIFICACIONES_NO_LEIDAS,false, Configuration.this));
        //cbPasos.setChecked(Sys.getBoolean(Sys.SETTINGS_PASOS,false, Configuration.this));
        cbTorch.setChecked(Sys.getBoolean(Sys.SETTINGS_TORCH,false, Configuration.this));
        cbDND.setChecked(Sys.getBoolean(Sys.SETTINGS_DND,false, Configuration.this));
        cbHalo.setChecked(Sys.getBoolean(Sys.SETTINGS_HALO,true, Configuration.this));
//        divisiones.setSelection(Sys.getInt(Sys.SETTINGS_DIVISIONES,2, Configuration.this));

        cbNumeroNotificaciones.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sys.save(Sys.SETTINGS_NUMERO_NOTIFICACIONES, isChecked, Configuration.this);
            }
        });

        cbNotificacionesNoLeidas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sys.save(Sys.SETTINGS_NOTIFICACIONES_NO_LEIDAS, isChecked, Configuration.this);
            }
        });

//        cbPasos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Sys.save(Sys.SETTINGS_PASOS, isChecked, Configuration.this);
//            }
//        });

        cbTorch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sys.save(Sys.SETTINGS_TORCH, isChecked, Configuration.this);
            }
        });

        cbDND.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sys.save(Sys.SETTINGS_DND, isChecked, Configuration.this);
            }
        });

        cbHalo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sys.save(Sys.SETTINGS_HALO, isChecked, Configuration.this);
            }
        });

//        divisiones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                                 @Override
//                                                 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                                     Sys.save(Sys.SETTINGS_DIVISIONES,position, Configuration.this);
//                                                     //startActivity(new Intent(Configuration.this, Test.class));
//                                                 }
//
//                                                 @Override
//                                                 public void onNothingSelected(AdapterView<?> parent) {
//
//                                                 }
//                                             }
//
//        );

    }


}