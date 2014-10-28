package py.fpuna.tesis.qoetest.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

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

public class EnviarTestActivity extends ActionBarActivity {
    private Button enviarTest;
    private WSHelper wsHelper;
    private PerfilUsuario perfilUsuario;
    private PhoneInfo phoneInfo;
    private List<PruebaTest> pruebaTest;
    private List<QoSParam> qoSParams;
    private DeviceLocation deviceLocation;
    private DeviceStatus deviceStatus;

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


        enviarTest = (Button) findViewById(R.id.startWebTestBtn);
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

            wsHelper.enviarResultados(perfilUsuario, phoneInfo, deviceStatus,
                    deviceLocation, pruebaTest, qoSParams);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}
