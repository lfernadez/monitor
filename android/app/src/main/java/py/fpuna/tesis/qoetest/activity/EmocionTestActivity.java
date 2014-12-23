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
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.location.LocationUtils;
import py.fpuna.tesis.qoetest.model.DeviceLocation;
import py.fpuna.tesis.qoetest.model.DeviceStatus;
import py.fpuna.tesis.qoetest.model.IperfTCPResults;
import py.fpuna.tesis.qoetest.model.IperfUDPResults;
import py.fpuna.tesis.qoetest.model.NetworkStat;
import py.fpuna.tesis.qoetest.model.PerfilUsuario;
import py.fpuna.tesis.qoetest.model.PhoneInfo;
import py.fpuna.tesis.qoetest.model.PingResults;
import py.fpuna.tesis.qoetest.model.QoSParam;
import py.fpuna.tesis.qoetest.rest.WSHelper;
import py.fpuna.tesis.qoetest.services.MonitoringService;
import py.fpuna.tesis.qoetest.services.NetworkMonitoringService;
import py.fpuna.tesis.qoetest.utils.Constants;
import py.fpuna.tesis.qoetest.utils.DeviceInfoUtils;
import py.fpuna.tesis.qoetest.utils.DeviceStatusUtils;
import py.fpuna.tesis.qoetest.utils.NetworkUtils;
import py.fpuna.tesis.qoetest.utils.PreferenceUtils;

public class EmocionTestActivity extends Activity {

    private PerfilUsuario perfilUsuario;
    private RadioGroup radioGroup;
    private TextView estadoLabel;
    private Button atrasBtn;
    private Button siguienteBtn;
    private String estado;

    MonitoringService mService;
    NetworkMonitoringService mNetworkService;
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private float bandwidth;
    private DeviceInfoUtils deviceInfo;
    private double cpuLoad;
    private double memLoad;
    private int signalLevel3G;
    private int signalLevelWifi;
    private int signalLevelActiveNetwork;
    private long dBm3G;
    private int cellID;
    private LocationUtils locationUtils;
    private Location currentLocation;
    private PreferenceUtils preferenceUtils;
    private PhoneInfo info;
    private String redActiva;
    private DeviceStatusUtils deviceStatsUtils;
    private NetworkUtils networkUtils;
    private int bateryLevel;
    private PingResults pingResults;
    private IperfTCPResults iperfTCPResults;
    private IperfUDPResults iperfUDPResults;

    private WSHelper wsHelper;
    private NetworkStat parametrosNet;

    private boolean mBound;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
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
            NetworkMonitoringService.NetworkServiceBinder binder =
                    (NetworkMonitoringService.NetworkServiceBinder) service;
            mNetworkService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emocion_test);

        perfilUsuario = getIntent().getParcelableExtra(Constants
                .EXTRA_PERFIL_USUARIO);

        estadoLabel = (TextView) findViewById(R.id.estado_label);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio0:
                        estadoLabel.setText("Alegre");
                        estado = "Alegre";
                        break;
                    case R.id.radio1:
                        estadoLabel.setText("Triste");
                        estado = "Triste";
                        break;
                    case R.id.radio2:
                        estadoLabel.setText("Enojado");
                        estado = "Enojado";
                        break;
                    case R.id.radio3:
                        estadoLabel.setText("Indiferente");
                        estado = "Indiferente";
                        break;
                }

            }
        });

        /* Boton Atras */
        atrasBtn = (Button) findViewById(R.id.leftButton);
        atrasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* Boton Siguiente */
        siguienteBtn = (Button) findViewById(R.id.rightButton);
        siguienteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificar()) {
                    new ObtenerParametrosTask().execute();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Seleccione un estado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Se abre el Shaerd Preferences
        preferenceUtils = new PreferenceUtils(this);
        mPrefs = getSharedPreferences(Constants.SAHRED_PREFERENCES,
                Context.MODE_PRIVATE);
        // Se Obtiene el editor del
        mEditor = mPrefs.edit();

        // Se crea el objeto LocationUtils
        locationUtils = new LocationUtils(getApplicationContext());
        locationUtils.connect();

        /** Progress Dialog */
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Obteniendo Localizacion...");

        /**Device Info Utils */
        deviceInfo = new DeviceInfoUtils(getApplicationContext());

        /** Device status utils */
        deviceStatsUtils = new DeviceStatusUtils(this);

        /** Network Utils */
        networkUtils = new NetworkUtils(this);

        wsHelper = new WSHelper();

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
    public boolean verificar() {
        return radioGroup.getCheckedRadioButtonId() != -1;
    }

    public class ObtenerParametrosTask extends AsyncTask<Void, Integer,
            Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Se obtiene los parametros de la red
                parametrosNet = wsHelper.obtenerParametros();
                publishProgress(0);
                currentLocation = locationUtils.getLastLocation();
                publishProgress(1);
                pingResults = mService.executePing();

                /* Iperf test */
                publishProgress(2);
                iperfTCPResults = mService.executeIperfTCP();
                publishProgress(3);
                iperfUDPResults = mService.executeIperfUDP();

                //bandwidth = mService.getBandwidth();
                //Log.d("BANWIDTH", String.valueOf(bandwidth));

                /* Se obtienen los datos del usuario del Preference Shared */
                if (perfilUsuario == null) {
                    perfilUsuario = preferenceUtils.getPerfilUsuario();
                }

                perfilUsuario.setEmociones(estado);

                if (mPrefs.contains(Constants.DEVICE_SHARED)) {
                    info = preferenceUtils.getDeviceInfo();
                } else {
                    info = deviceInfo.getPhoneInfo();
                    preferenceUtils.savePhoneInfo(info);
                }
                // Uso de CPU
                cpuLoad = mService.getCpuLoad();

                //Carga de Memoria
                double memFree = mService.getMemFree();
                double totalram = deviceInfo.getRAMProc();
                memLoad = ((totalram - memFree) / totalram) * 100;

                //Nivel de senal red celular
                signalLevel3G = mNetworkService.getSignalLevel3G();

                //Nivel de senal red WiFi
                signalLevelWifi = mNetworkService.getSignalLevelWiFi();

                //Nivel de senal red celular dBm
                dBm3G = mNetworkService.getdBm3G();

                //Nivel de senal de la red activa
                signalLevelActiveNetwork = mNetworkService
                        .getSignalLevelActiveNetwork();

                //Red Activa
                redActiva = mNetworkService.getActiveNetwork();

                //Nivel de Bateria
                bateryLevel = deviceStatsUtils.getBateryLevel();

                //ID Celda
                cellID = networkUtils.getCID();

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
            if (values[0] == 1) {
                progressDialog.setMessage("Realizando test Ping");
            }
            if (values[0] == 2) {
                progressDialog.setMessage("Realizando Speed Test TCP");
            }
            if (values[0] == 3) {
                progressDialog.setMessage("Realizando Speed Test UDP");
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void res) {
            progressDialog.dismiss();

            Intent intent = new Intent(getApplicationContext(),
                    WebTestIntroActivity.class);

            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                extras = new Bundle();
            }

            // Extra del Perfil del Usuario
            extras.putParcelable(Constants.EXTRA_PERFIL_USUARIO,
                    perfilUsuario);
            // Extra de la Informacion del dispositivo
            extras.putParcelable(Constants.EXTRA_DEVICE_INFO, info);

            // Extra del estado del dispositivo
            DeviceStatus status = new DeviceStatus();
            status.setUsoCpu(cpuLoad);
            status.setUsoRam(memLoad);
            status.setTipoAccesoInternet(redActiva);
            status.setIntensidadSenhal(String.valueOf
                    (signalLevelActiveNetwork));
            status.setNivelBateria(bateryLevel);
            extras.putParcelable(Constants.EXTRA_DEVICE_STATUS, status);

            // Extra de la localizacion del usuario
            DeviceLocation loc = new DeviceLocation();
            if (currentLocation == null) {
                loc.setLatitud(0.0);
                loc.setLongitud(0.0);
            } else {
                loc.setLatitud(currentLocation.getLatitude());
                loc.setLongitud(currentLocation.getLongitude());
            }

            loc.setIdCelda(cellID);
            extras.putParcelable(Constants.EXTRA_LOCALIZACION, loc);

            // Extra de parametros QoS
            ArrayList<QoSParam> parametrosQos = new ArrayList<QoSParam>();

            //Delay
            QoSParam delayParam = new QoSParam();
            delayParam.setCodigoParametro(Constants.DELAY_ID);
            delayParam.setValor(pingResults.getRttAvg());
            delayParam.setObtenido(Constants.OBT_TEL);

            // Bandwidth
            QoSParam bandwidthParam = new QoSParam();
            bandwidthParam.setCodigoParametro(Constants.BANDWITDH_ID);
            bandwidthParam.setValor(iperfTCPResults.getBandwidthDown());
            bandwidthParam.setObtenido(Constants.OBT_TEL);

            // Packet Loss
            QoSParam packetLossParam = new QoSParam();
            packetLossParam.setCodigoParametro(Constants.PACKET_LOSS_ID);
            packetLossParam.setValor(iperfUDPResults.getPacketLoss());
            packetLossParam.setObtenido(Constants.OBT_TEL);

            // Jitter
            QoSParam jitterParam = new QoSParam();
            jitterParam.setCodigoParametro(Constants.JITTER_ID);
            jitterParam.setValor(iperfUDPResults.getJitter());
            jitterParam.setObtenido(Constants.OBT_TEL);

            /* Enviados desde el servidor */

            /** Delay
            QoSParam delayServerParam = new QoSParam();
            delayServerParam.setCodigoParametro(Constants.DELAY_ID);
            delayServerParam.setValor(parametrosNet.getDelay());
            delayServerParam.setObtenido(Constants.OBT_ENV);

            // Bandwidth
            QoSParam bandwidthServerParam = new QoSParam();
            bandwidthServerParam.setCodigoParametro(Constants.BANDWITDH_ID);
            bandwidthServerParam.setValor(parametrosNet.getkbpsDown());
            bandwidthServerParam.setObtenido(Constants.OBT_ENV);

            // Packet Loss
            QoSParam packetLossServerParam = new QoSParam();
            packetLossServerParam.setCodigoParametro(Constants.PACKET_LOSS_ID);
            packetLossServerParam.setValor(parametrosNet.getPacketLoss());
            packetLossServerParam.setObtenido(Constants.OBT_ENV);

            // Jitter
            QoSParam jitterServerParam = new QoSParam();
            jitterServerParam.setCodigoParametro(Constants.JITTER_ID);
            jitterServerParam.setValor(parametrosNet.getJitter());
            jitterServerParam.setObtenido(Constants.OBT_ENV);*/


            // Se agregan los parametros
            parametrosQos.add(delayParam);
            parametrosQos.add(bandwidthParam);
            parametrosQos.add(packetLossParam);
            parametrosQos.add(jitterParam);
            /*parametrosQos.add(delayServerParam);
            parametrosQos.add(bandwidthServerParam);
            parametrosQos.add(packetLossServerParam);
            parametrosQos.add(jitterServerParam);*/
            extras.putParcelableArrayList(Constants.EXTRA_PARAM_QOS,parametrosQos);

            intent.putExtras(extras);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

}
