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

import java.io.IOException;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.location.LocationUtils;
import py.fpuna.tesis.qoetest.model.PerfilUsuario;
import py.fpuna.tesis.qoetest.model.PingResults;
import py.fpuna.tesis.qoetest.services.MonitoringService;
import py.fpuna.tesis.qoetest.utils.Constants;

public class PreTestActivty extends Activity {

    public static final String SPIN_SEXO_POS = "spinner_sexo_pos";
    public static final String SPIN_PROF_POS = "spinner_prof_pos";
    public static final String SPIN_FREC_POS = "spinner_frec_pos";
    public static final String EDAD = "edad";


    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    private Spinner spinnerSexo;
    private Spinner spinnerProfesion;
    private Spinner spinnerFrecuencia;
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

    MonitoringService mService;
    private boolean mBound;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MonitoringService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
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
            currentLocation = locationUtils.getLastLocation();
            publishProgress(0);
            /*try {
                pingResults = mService.executePing();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            publishProgress(1);
            bandwidth = mService.getBandwidth();
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
            Log.d("Ancho de Banda", String.valueOf(bandwidth));
            PerfilUsuario perfilUsuario = new PerfilUsuario();
            perfilUsuario.setSexo(sexo);
            perfilUsuario.setEdad(Integer.valueOf(edad));
            perfilUsuario.setProfesion(profesion);
            perfilUsuario.setFrecuenciaUso(frecuencia);
            Intent intent = new Intent(getApplicationContext(),
                    WebTestIntroActivity.class);
            intent.putExtra(WebTestIntroActivity.EXTRA_PERFIL_USUARIO,
                    perfilUsuario);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }


}
