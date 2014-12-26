package py.fpuna.tesis.qoetest.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.model.DeviceLocation;
import py.fpuna.tesis.qoetest.model.DeviceStatus;
import py.fpuna.tesis.qoetest.model.PerfilUsuario;
import py.fpuna.tesis.qoetest.model.PhoneInfo;
import py.fpuna.tesis.qoetest.model.PruebaTest;
import py.fpuna.tesis.qoetest.model.QoSParam;
import py.fpuna.tesis.qoetest.rest.WSHelper;
import py.fpuna.tesis.qoetest.utils.Constants;
import py.fpuna.tesis.qoetest.utils.DateHourUtils;

public class EnviarTestActivity extends Activity {
    private Button enviarTest;
    private WSHelper wsHelper;
    private PerfilUsuario perfilUsuario;
    private PhoneInfo phoneInfo;
    private List<PruebaTest> pruebaTest;
    private List<QoSParam> qoSParams;
    private DeviceLocation deviceLocation;
    private DeviceStatus deviceStatus;
    private ProgressDialog progressDialog;
    private Button atrasButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_test);

        Bundle bundle = getIntent().getExtras();
        /** Valores de MOS de las pruebas */
        pruebaTest = bundle.getParcelableArrayList(Constants.EXTRA_QOE_TEST);
        /** Parametros QoS */
        qoSParams = bundle.getParcelableArrayList(Constants.EXTRA_PARAM_QOS);
        /** Perfil del Usuario */
        perfilUsuario = bundle.getParcelable(Constants.EXTRA_PERFIL_USUARIO);
        /** Informacion del dispositivo */
        phoneInfo = bundle.getParcelable(Constants.EXTRA_DEVICE_INFO);
        /** Estado del dispositivo */
        deviceStatus = bundle.getParcelable(Constants.EXTRA_DEVICE_STATUS);
        /** Localizacion del dispositivo */
        deviceLocation = bundle.getParcelable(Constants.EXTRA_LOCALIZACION);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Enviando resultados...");

        atrasButton = (Button) findViewById(R.id.leftButton);
        atrasButton.setVisibility(View.GONE);
        enviarTest = (Button) findViewById(R.id.rightButton);
        enviarTest.setText(getString(R.string.enviar_resultados_label));
        enviarTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EnviarResultados().execute();
            }
        });
    }

    public class EnviarResultados extends AsyncTask<Void, Void,
            Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            wsHelper = new WSHelper();

            Date fecha = new Date();
            Date hora = new Date();

            String fechaString = DateHourUtils.format(fecha,
                    DateHourUtils.Format.DATE_STORE);
            String horaString = DateHourUtils.format(hora,
                    DateHourUtils.Format.TIME_STORE);

            phoneInfo.setEstadoTelefono(deviceStatus);
            phoneInfo.setLocalizacion(deviceLocation);

            wsHelper.enviarResultados(perfilUsuario, phoneInfo, deviceStatus,
                    deviceLocation, pruebaTest, qoSParams, fechaString, horaString);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            Toast.makeText(getBaseContext(), "Datos enviados",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),
                    PrincipalActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            EnviarTestActivity.this.finish();
        }
    }
}
