package emperatriz.pypots;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import emperatriz.pypots.common.Sys;

public class Configuration extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        CheckBox cbNumeroNotificaciones = (CheckBox)findViewById(R.id.cbNumeroNotificaciones);
        CheckBox cbNotificacionesNoLeidas = (CheckBox)findViewById(R.id.cbNotificacionesNoLeidas);
        CheckBox cbPasos = (CheckBox)findViewById(R.id.cbPasos);

        cbNumeroNotificaciones.setChecked(Sys.getBoolean(Sys.SETTINGS_NUMERO_NOTIFICACIONES,false, Configuration.this));
        cbNotificacionesNoLeidas.setChecked(Sys.getBoolean(Sys.SETTINGS_NOTIFICACIONES_NO_LEIDAS,false, Configuration.this));
        cbPasos.setChecked(Sys.getBoolean(Sys.SETTINGS_PASOS,false, Configuration.this));

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

        cbPasos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sys.save(Sys.SETTINGS_PASOS, isChecked, Configuration.this);
            }
        });

    }


}