package py.fpuna.tesis.qoetest.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.location.LocationUtils;
import py.fpuna.tesis.qoetest.model.PerfilUsuario;
import py.fpuna.tesis.qoetest.model.PhoneInfo;
import py.fpuna.tesis.qoetest.model.PingResults;
import py.fpuna.tesis.qoetest.services.MonitoringService;
import py.fpuna.tesis.qoetest.services.NetworkMonitoringService;
import py.fpuna.tesis.qoetest.ui.MultiSelectionSpinner;
import py.fpuna.tesis.qoetest.utils.Constants;
import py.fpuna.tesis.qoetest.utils.DeviceInfoUtils;

public class PreTestActivty extends Activity {

    public static final String SPIN_SEXO_POS = "spinner_sexo_pos";
    public static final String SPIN_PROF_POS = "spinner_prof_pos";
    public static final String SPIN_FREC_POS = "spinner_frec_pos";
    public static final String EDAD = "edad";
    public static final String SPIN_FREC_APPS = "spinner_frec_apps";


    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    private Spinner spinnerSexo;
    private Spinner spinnerProfesion;
    private Spinner spinnerFrecuencia;
    private MultiSelectionSpinner spinnerApp;
    private EditText edadEditText;
    private Button atrasBtn;
    private Button siguienteBtn;
    private String sexo;
    private int sexoPos;
    private String edad;
    private String profesion;
    private int profesionPos;
    private String frecuencia;
    private int frecuenciaPos;
    private LocationUtils locationUtils;
    private Location currentLocation;
    private ProgressDialog progressDialog;
    private PingResults pingResults;
    private float bandwidth;
    private PhoneInfo info;
    private DeviceInfoUtils deviceInfo;
    private long cpuLoad;
    private long memLoad;
    private String[] apps;
    private String selectedApps;

    MonitoringService mService;
    NetworkMonitoringService mNetworkService;

    private boolean mBound;

    private int signalLevel3G;
    private int signalLevelWifi;
    private long dBm3G;

    public static final String TAG = "PreTestActivity";

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MonitoringService.LocalBinder binder = (MonitoringService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    private ServiceConnection mNetworkServiceConnection = new ServiceConnection
            () {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            // We've bound to LocalService, cast the IBinder and get LocalService instance
            NetworkMonitoringService.NetworkServiceBinder binder =
                    (NetworkMonitoringService.NetworkServiceBinder) service;
            mNetworkService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_pre_test_activty);

        /* Spinner para la seleccion del Sexo */
        spinnerSexo = (Spinner) findViewById(R.id.spinner_sexo);
        ArrayAdapter<String> adapter = getSpinnerAdapterSexo();
        spinnerSexo.setAdapter(adapter);
        spinnerSexo.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sexoPos = i;
                sexo = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /** Device Info Utils */
        deviceInfo = new DeviceInfoUtils(getApplicationContext());

        /** Progress Dialog */
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Obteniendo Localizacion...");

        /* Spinner para la seleccion de la Profesion */
        spinnerProfesion = (Spinner) findViewById(R.id.spinner_profesion);
        ArrayAdapter<String> adapterProfesion = getSpinnerAdapterProfesion();
        spinnerProfesion.setAdapter(adapterProfesion);
        spinnerProfesion.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                profesionPos = i;
                profesion = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /* Spinner para la seleccion de la frecuencia de utilizacion */
        spinnerFrecuencia = (Spinner) findViewById(R.id.spinner_frecuencua);
        ArrayAdapter<String> adapterFrecuencia = getSpinnerAdapterFrecuencia();
        spinnerFrecuencia.setAdapter(adapterFrecuencia);
        spinnerFrecuencia.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                frecuenciaPos = i;
                frecuencia = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edadEditText = (EditText) findViewById(R.id.edad_editText);

        /** Spinner para seleccion de apps */
        spinnerApp = (MultiSelectionSpinner) findViewById(R.id.spinner_frecuencia_app);
        apps = getResources().getStringArray(R.array.apps);
        spinnerApp.setItems(apps);

        /* Boton Atras */
        atrasBtn = (Button) findViewById(R.id.leftButton);
        atrasBtn.setVisibility(View.INVISIBLE);

        /* Boton Siguiente */
        siguienteBtn = (Button) findViewById(R.id.rightButton);
        siguienteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edad = edadEditText.getText().toString();
                if (edad == null || TextUtils.isEmpty(edad)) {
                    edadEditText.setError("Complete su edad");
                } else {
                    new ObtenerParametrosTask().execute();

                }
            }
        });

        // Se abre el Shaerd Preferences
        mPrefs = getSharedPreferences(Constants.SAHRED_PREFERENCES,
                Context.MODE_PRIVATE);
        // Se Obtiene el editor del
        mEditor = mPrefs.edit();

        // Se crea el objeto LocationUtils
        locationUtils = new LocationUtils(this);
        locationUtils.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEditor.putInt(SPIN_SEXO_POS, spinnerSexo.getSelectedItemPosition());
        mEditor.putInt(SPIN_PROF_POS, spinnerProfesion
                .getSelectedItemPosition());
        mEditor.putInt(SPIN_FREC_POS, spinnerFrecuencia
                .getSelectedItemPosition());
        mEditor.putString(EDAD, edad);
        Set<String> apps = new HashSet<String>();
        for(String app : spinnerApp.getSelectedStrings()){
            apps.add(app);
        }
        mEditor.putStringSet(SPIN_FREC_APPS, apps);
        mEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPrefs.contains(SPIN_SEXO_POS)) {
            spinnerSexo.setSelection(mPrefs.getInt(SPIN_SEXO_POS, 0));
        }
        if (mPrefs.contains(SPIN_PROF_POS)) {
            spinnerProfesion.setSelection(mPrefs.getInt(SPIN_PROF_POS, 0));
        }
        if (mPrefs.contains(SPIN_FREC_POS)) {
            spinnerFrecuencia.setSelection(mPrefs.getInt(SPIN_FREC_POS, 0));
        }
        if (mPrefs.contains(EDAD)) {
            edadEditText.setText(mPrefs.getString(EDAD, "0"));
        }
        if(mPrefs.contains(SPIN_FREC_APPS)){
            Set<String> apps = mPrefs.getStringSet(SPIN_FREC_APPS,
                    new HashSet<String>());
            List<String> frecApps = new ArrayList<String>();
            frecApps.addAll(apps);
            spinnerApp.setSelection(frecApps);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MonitoringService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Intent intentNetworkService = new Intent(this, NetworkMonitoringService.class);
        bindService(intentNetworkService, mNetworkServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            unbindService(mNetworkServiceConnection);
            mBound = false;
        }
    }

    /**
     * @return
     */
    private ArrayAdapter<String> getSpinnerAdapterSexo() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("MASCULINO");
        adapter.add("FEMENINO");
        return adapter;
    }

    /**
     * @return
     */
    private ArrayAdapter<String> getSpinnerAdapterProfesion() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("ESTUDIANTE");
        adapter.add("EMPLEADITO");
        return adapter;
    }

    /**
     * @return
     */
    private ArrayAdapter<String> getSpinnerAdapterFrecuencia() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("OBSESIONADO");
        adapter.add("DIARIAMENTE");
        adapter.add("OCASIONALMENTE");
        adapter.add("NUNCA");
        return adapter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                NavUtils.navigateUpTo(this, upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ObtenerParametrosTask extends AsyncTask<Void, Integer,
            Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                currentLocation = locationUtils.getLastLocation();
                publishProgress(0);
                //pingResults = mService.executePing();
                publishProgress(1);
                bandwidth = mService.getBandwidth();
                Log.d("BANWIDTH", String.valueOf(bandwidth));
                info = deviceInfo.getPhoneInfo();
                cpuLoad = mService.getCpuLoad();
                memLoad = mService.getMemFree();
                signalLevel3G = mNetworkService.getSignalLevel3G();
                signalLevelWifi = mNetworkService.getSignalLevelWiFi();
                dBm3G = mNetworkService.getdBm3G();
                Log.d(TAG + "3G Level", String.valueOf(signalLevel3G));
                Log.d(TAG + "WiFi Level", String.valueOf(signalLevelWifi));
                Log.d(TAG + "dBm 3G", String.valueOf(dBm3G));
                savePerfilShared();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[0] == 0) {
                progressDialog.setMessage("Realizando test Ping");
            }
            if (values[0] == 1) {
                progressDialog.setMessage("Realizando Speed Test");
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void res) {
            progressDialog.dismiss();

            /* Perfil de Usuario */
            PerfilUsuario perfilUsuario = new PerfilUsuario();
            perfilUsuario.setSexo(sexo);
            perfilUsuario.setEdad(Integer.valueOf(edad));
            perfilUsuario.setProfesion(profesion);
            perfilUsuario.setFrecuenciaUso(frecuencia);
            perfilUsuario.setAplicacionesFrecuentes(spinnerApp.getSelectedItemsAsString());

            //selectedApps = spinnerApp.getSelectedItemsAsString();

            mEditor.putBoolean("EXISTE_SHARED", true);
            mEditor.commit();

            Intent intent = new Intent(getApplicationContext(),
                    WebTestIntroActivity.class);
            intent.putExtra(WebTestIntroActivity.EXTRA_PERFIL_USUARIO,
                    perfilUsuario);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public void savePerfilShared() {
        Gson gson = new Gson();
        PerfilUsuario perfilUsuario = new PerfilUsuario();
        perfilUsuario.setSexo(sexo);
        perfilUsuario.setEdad(Integer.valueOf(edad));
        perfilUsuario.setProfesion(profesion);
        perfilUsuario.setFrecuenciaUso(frecuencia);
        perfilUsuario.setAplicacionesFrecuentes(spinnerApp.getSelectedItemsAsString());

        mEditor.putString(Constants.PERFIL_USUARIO_SHARED, gson.toJson(perfilUsuario));
        mEditor.commit();
    }

}
