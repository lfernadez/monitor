package py.fpuna.tesis.qoetest.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.location.LocationUtils;
import py.fpuna.tesis.qoetest.model.PerfilUsuario;

public class PreTestActivty extends Activity {

    public static final String SEXO_STATE = "sexo_state";
    public static final String PROFESION_STATE = "profesion_state";
    public static final String EDAD_STATE = "edad_state";
    public static final String FRECUENCIA_STATE = "frecuencia_state";
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

    public static final String TAG = "PreTestActivity";

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
                    new ObtenerLocalizacionTask().execute();

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
        });

        // Se abre el Shaerd Preferences
        mPrefs = getSharedPreferences(LocationUtils.SAHRED_PREFERENCES,
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.pre_test, menu);
        return true;
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

    public class ObtenerLocalizacionTask extends AsyncTask<Void, Void,
            Location>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Location doInBackground(Void... voids) {
            return locationUtils.getLastLocation();
        }

        @Override
        protected void onPostExecute(Location location) {
            progressDialog.dismiss();
            currentLocation = location;
            Log.d(TAG, "Longitud: " + String.valueOf(currentLocation
                    .getLongitude()));
            Log.d(TAG, "Latitud: " + String.valueOf(currentLocation
                    .getLatitude()));
        }
    }

}
