package py.fpuna.tesis.qoetest.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.model.PerfilUsuario;
import py.fpuna.tesis.qoetest.ui.MultiSelectionSpinner;
import py.fpuna.tesis.qoetest.utils.Constants;
import py.fpuna.tesis.qoetest.utils.PreferenceUtils;

public class PreTestActivty extends ActionBarActivity {

    public static final String SPIN_SEXO_POS = "spinner_sexo_pos";
    public static final String SPIN_PROF_POS = "spinner_prof_pos";
    public static final String SPIN_FREC_POS = "spinner_frec_pos";
    public static final String SPIN_COSTO = "spinner_costo";
    public static final String SPIN_COSTO_MAS = "spinner_costo_mas";
    public static final String EDAD = "edad";
    public static final String SPIN_FREC_APPS = "spinner_frec_apps";
    public static final String TAG = "PreTestActivity";
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    private Spinner spinnerSexo;
    private Spinner spinnerProfesion;
    private Spinner spinnerFrecuencia;
    private Spinner spinnerCosto;
    private Spinner spinnerCostoMas;
    private MultiSelectionSpinner spinnerApp;
    private EditText edadEditText;
    private Button atrasBtn;
    private Button siguienteBtn;
    private String sexo;
    private String edad;
    private String profesion;
    private String frecuencia;
    private String respuestaCostoMas;
    private String respuestaCosto;
    private String[] apps;


    private PreferenceUtils preferenceUtils;
    private PerfilUsuario perfilUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_pre_test_activty);

        /* Spinner para la seleccion del Sexo */
        spinnerSexo = (Spinner) findViewById(R.id.spinner_sexo);
        ArrayAdapter<String> adapter = getSpinnerAdapterSexo();
        spinnerSexo.setAdapter(adapter);
        spinnerSexo.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sexo = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /* Spinner para la seleccion de la Profesion */
        spinnerProfesion = (Spinner) findViewById(R.id.spinner_profesion);
        ArrayAdapter<String> adapterProfesion = getSpinnerAdapterProfesion();
        spinnerProfesion.setAdapter(adapterProfesion);
        spinnerProfesion.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
                frecuencia = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /** Spinner de Costo */
        spinnerCosto = (Spinner) findViewById(R.id.spinner_costo);
        ArrayAdapter<String> adapterCosto = getSpinnerAdapterCosto();
        spinnerCosto.setAdapter(adapterCosto);
        spinnerCosto.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                respuestaCosto = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /** Spinner de Costo */
        spinnerCostoMas = (Spinner) findViewById(R.id.spinner_costo_mas);
        ArrayAdapter<String> adapterCostoMas = getSpinnerAdapterCostoMas();
        spinnerCostoMas.setAdapter(adapterCostoMas);
        spinnerCostoMas.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                respuestaCostoMas = adapterView.getItemAtPosition(i).toString();
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
                if (verificar()) {
                    /** Se guarda el perfil del usuario en el Shared Preferences */
                    perfilUsuario = new PerfilUsuario();
                    perfilUsuario.setSexo(sexo);
                    perfilUsuario.setEdad(Integer.valueOf(edad));
                    perfilUsuario.setProfesion(profesion);
                    perfilUsuario.setFrecuenciaUso(frecuencia);
                    perfilUsuario.setAplicacionesFrecuentes(spinnerApp.getSelectedItemsAsString());
                    perfilUsuario.setRespuestaCosto(respuestaCosto);
                    perfilUsuario.setRespuestaCostoMas(respuestaCostoMas);
                    preferenceUtils.savePerfilUsuario(perfilUsuario);
                    mEditor.putBoolean("EXISTE_SHARED", true);
                    mEditor.commit();

                    Bundle extras = getIntent().getExtras();
                    if (extras == null) {
                        extras = new Bundle();
                    }

                    extras.putParcelable(Constants.EXTRA_PERFIL_USUARIO, perfilUsuario);
                    Intent intent = new Intent(getApplicationContext(),
                            EmocionTestActivity.class);
                    intent.putExtras(extras);
                    startActivity(intent);

                } else {
                    if (edad == null || TextUtils.isEmpty(edad)) {
                        edadEditText.setError("Complete su ");
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Complete todos los campos",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        // Se abre el Shaerd Preferences
        preferenceUtils = new PreferenceUtils(this);
        mPrefs = getSharedPreferences(Constants.SAHRED_PREFERENCES,
                Context.MODE_PRIVATE);
        // Se Obtiene el editor del
        mEditor = mPrefs.edit();
    }

    public boolean verificar() {
        edad = edadEditText.getText().toString();
        if (edad == null || TextUtils.isEmpty(edad)) {
            return false;
        }
        if (spinnerApp.getSelectedStrings().isEmpty()) {
            return false;
        }
        return true;
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
        for (String app : spinnerApp.getSelectedStrings()) {
            apps.add(app);
        }
        mEditor.putInt(SPIN_COSTO, spinnerCosto.getSelectedItemPosition());
        mEditor.putInt(SPIN_COSTO_MAS, spinnerCostoMas.getSelectedItemPosition());
        if (Build.VERSION.SDK_INT >= 11) {
            mEditor.putStringSet(SPIN_FREC_APPS, apps);
        } else {
            mEditor.putString(SPIN_FREC_APPS, spinnerApp.getSelectedItemsAsString());
        }
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
        if (mPrefs.contains(SPIN_COSTO)) {
            spinnerCosto.setSelection(mPrefs.getInt(SPIN_COSTO, 0));
        }
        if (mPrefs.contains(SPIN_COSTO_MAS)) {
            spinnerCostoMas.setSelection(mPrefs.getInt(SPIN_COSTO_MAS, 0));
        }
        if (mPrefs.contains(EDAD)) {
            edadEditText.setText(mPrefs.getString(EDAD, "0"));
        }
        if (mPrefs.contains(SPIN_FREC_APPS)) {
            if (Build.VERSION.SDK_INT >= 11) {
                Set<String> apps = mPrefs.getStringSet(SPIN_FREC_APPS,
                        new HashSet<String>());
                List<String> frecApps = new ArrayList<String>();
                frecApps.addAll(apps);
                spinnerApp.setSelection(frecApps);
            } else {
                String apps = mPrefs.getString(SPIN_FREC_APPS, new String());
                String[] frecApps = apps.split(", ");
                spinnerApp.setSelection(frecApps);
            }
        }
    }

    /**
     * @return
     */
    private ArrayAdapter<String> getSpinnerAdapterSexo() {
        if (Build.VERSION.SDK_INT >= 11) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            String[] sexo = getResources().getStringArray(R.array.sexo);
            adapter.addAll(sexo);
            return adapter;
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            String[] sexo = getResources().getStringArray(R.array.sexo);
            for (String s : sexo) {
                adapter.add(s);
            }
            return adapter;
        }
    }

    /**
     * @return
     */
    private ArrayAdapter<String> getSpinnerAdapterProfesion() {
        if (Build.VERSION.SDK_INT >= 11) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            String[] profesion = getResources().getStringArray(R.array.prefesion);
            adapter.addAll(profesion);
            return adapter;
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            String[] profesion = getResources().getStringArray(R.array.prefesion);
            for (String p : profesion) {
                adapter.add(p);
            }
            return adapter;
        }
    }

    /**
     * @return
     */
    private ArrayAdapter<String> getSpinnerAdapterFrecuencia() {
        if (Build.VERSION.SDK_INT >= 11) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            String[] frecuencia = getResources().getStringArray(R.array.frecuencia);
            adapter.addAll(frecuencia);
            return adapter;
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            String[] frecuencia = getResources().getStringArray(R.array.frecuencia);
            for (String f : frecuencia) {
                adapter.add(f);
            }
            return adapter;
        }
    }

    private ArrayAdapter<String> getSpinnerAdapterCosto() {
        if (Build.VERSION.SDK_INT >= 11) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            String[] costo = getResources().getStringArray(R.array.costo);
            adapter.addAll(costo);
            return adapter;
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            String[] costo = getResources().getStringArray(R.array.costo);
            for (String c : costo) {
                adapter.add(c);
            }
            return adapter;
        }
    }

    private ArrayAdapter<String> getSpinnerAdapterCostoMas() {
        if (Build.VERSION.SDK_INT >= 11) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            String[] costo = getResources().getStringArray(R.array.costoMas);
            adapter.addAll(costo);
            return adapter;
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            String[] costo = getResources().getStringArray(R.array.costoMas);
            for (String c : costo) {
                adapter.add(c);
            }
            return adapter;
        }
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

    private String getString(String[] array, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String s : array) {
            if (sb.length() > 0) {
                sb.append(delimiter);
            }
            sb.append(s);
        }

        return sb.toString();
    }

    private String[] getArray(String input, String delimiter) {
        return input.split(delimiter);
    }
}
