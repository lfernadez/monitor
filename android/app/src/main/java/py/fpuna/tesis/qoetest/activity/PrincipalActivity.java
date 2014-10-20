package py.fpuna.tesis.qoetest.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;

import java.io.IOException;
import java.util.ArrayList;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.fragment.InfoFragment;
import py.fpuna.tesis.qoetest.fragment.NavigationDrawerFragment;
import py.fpuna.tesis.qoetest.fragment.TestFragment;
import py.fpuna.tesis.qoetest.location.LocationUtils;
import py.fpuna.tesis.qoetest.model.DeviceLocation;
import py.fpuna.tesis.qoetest.model.DeviceStatus;
import py.fpuna.tesis.qoetest.model.PerfilUsuario;
import py.fpuna.tesis.qoetest.model.PhoneInfo;
import py.fpuna.tesis.qoetest.model.PingResults;
import py.fpuna.tesis.qoetest.model.QoSParam;
import py.fpuna.tesis.qoetest.services.MonitoringService;
import py.fpuna.tesis.qoetest.services.NetworkMonitoringService;
import py.fpuna.tesis.qoetest.utils.Constants;
import py.fpuna.tesis.qoetest.utils.DeviceInfoUtils;
import py.fpuna.tesis.qoetest.utils.DeviceStatusUtils;
import py.fpuna.tesis.qoetest.utils.NetworkUtils;
import py.fpuna.tesis.qoetest.utils.PreferenceUtils;

public class PrincipalActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        InfoFragment.OnFragmentInteractionListener,
        TestFragment.OnClickStartButtonListener,
        TestFragment.OnFragmentInteractionListener {

    public static final String EXISTE_SHARED = "EXISTE_SHARED";
    public static final String TAG = "PrincipalActivity";

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
    private PerfilUsuario perfilUsuario;
    private PreferenceUtils preferenceUtils;
    private PhoneInfo info;
    private String redActiva;
    private DeviceStatusUtils deviceStatsUtils;
    private NetworkUtils networkUtils;
    private int bateryLevel;
    private PingResults pingResults;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private boolean mBound;
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

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        preferenceUtils = new PreferenceUtils(this);

        /*try {
            new Database(getBaseContext()).createDataBase(getBaseContext());
		} catch (IOException e) {
				e.printStackTrace();
		}*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        /* Verificacion de conexion */
        /*ConnectivityManager manager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);

        Boolean isWifi = manager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        Boolean is3g = manager.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if(!isWifi && !is3g){
            ConnectionErrorDialogFragment cedf = new
                    ConnectionErrorDialogFragment();
            cedf.show(getSupportFragmentManager(), "TAG");

        }*/

        // Se abre el Shaerd Preferences
        mPrefs = getSharedPreferences(Constants.SAHRED_PREFERENCES,
                Context.MODE_PRIVATE);
        // Se Obtiene el editor del
        mEditor = mPrefs.edit();

        // Se crea el objeto LocationUtils
        locationUtils = new LocationUtils(this);
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

        /* Se inician los servicios de monitoreo */
        Intent intent = new Intent(getApplicationContext(), MonitoringService.class);
        startService(intent);
        Intent intentNetworkService = new Intent(getApplicationContext(),
                NetworkMonitoringService.class);
        startService(intentNetworkService);
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

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, TestFragment.newInstance(position + 1))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, InfoFragment.newInstance(position + 1))
                        .commit();
                break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getResources().getStringArray(R.array.nav_drawer_items)[0];
                break;
            case 2:
                mTitle = getResources().getStringArray(R.array.nav_drawer_items)[1];
                break;
            case 3:
                mTitle = getResources().getStringArray(R.array.nav_drawer_items)[2];
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.principal, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFragmentInteraction(int number) {
        switch (number) {
            case 1:
                mTitle = getResources().getStringArray(R.array.nav_drawer_items)[0];
                break;
            case 2:
                mTitle = getResources().getStringArray(R.array.nav_drawer_items)[1];
                break;
            case 3:
                mTitle = getResources().getStringArray(R.array.nav_drawer_items)[2];
                break;
        }
    }

    @Override
    public void onClickStartButton() {
        if (mPrefs.contains(EXISTE_SHARED)) {
            new ObtenerParametrosTask().execute();
        } else {
            Intent intent = new Intent(this, PreTestActivty.class);
            startActivity(intent);
        }
    }


    public class ConnectionErrorDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity
                    ());
            builder.setMessage("No posee conexión, actívela")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().finish();
                        }
                    });
            return builder.create();

        }
    }

    public class ObtenerParametrosTask extends AsyncTask<Void, Integer,
            Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                currentLocation = locationUtils.getLastLocation();
                publishProgress(0);
                pingResults = mService.executePing();
                publishProgress(1);
                bandwidth = mService.getBandwidth();
                Log.d("BANWIDTH", String.valueOf(bandwidth));

                /* Se obtienen los datos del usuario del Preference Shared */
                perfilUsuario = preferenceUtils.getPerfilUsuario();

                if (mPrefs.contains(Constants.DEVICE_SHARED)) {
                    info = preferenceUtils.getDeviceInfo();
                } else {
                    info = deviceInfo.getPhoneInfo();
                    preferenceUtils.savePhoneInfo(info);
                }
                cpuLoad = mService.getCpuLoad();
                double memFree = mService.getMemFree();
                double totalram = deviceInfo.getRAMProc();
                memLoad = ((totalram - memFree) / totalram) * 100;
                signalLevel3G = mNetworkService.getSignalLevel3G();
                signalLevelWifi = mNetworkService.getSignalLevelWiFi();
                dBm3G = mNetworkService.getdBm3G();
                signalLevelActiveNetwork = mNetworkService
                        .getSignalLevelActiveNetwork();
                redActiva = mNetworkService.getActiveNetwork();
                bateryLevel = deviceStatsUtils.getBateryLevel();
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
            Intent intent = new Intent(getApplicationContext(),
                    WebTestIntroActivity.class);

            Bundle extras = new Bundle();
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
            status.setNivelBaterial(bateryLevel);
            extras.putParcelable(Constants.EXTRA_DEVICE_STATUS, status);

            // Extra de la localizacion del usuario
            DeviceLocation loc = new DeviceLocation();
            loc.setLatitud(currentLocation.getLatitude());
            loc.setLongitud(currentLocation.getLongitude());

            /*loc.setFecha(DateHourUtils.format(new Date(),
                    DateHourUtils.Format.DATE_VIEW));
            loc.setHora(DateHourUtils.format(new Date(),
                    DateHourUtils.Format.TIME_VIEW));*/
            loc.setIdCelda(cellID);
            extras.putParcelable(Constants.EXTRA_LOCALIZACION, loc);

            // Extra de parametros QoS
            ArrayList<QoSParam> parametrosQos = new ArrayList<QoSParam>();

            //Delay
            QoSParam delayParam = new QoSParam();
            delayParam.setCodigoParametro(Constants.DELAY_ID);
            delayParam.setValor(pingResults.getRttAvg());
            // Bandwidth
            QoSParam bandwidthParam = new QoSParam();
            bandwidthParam.setCodigoParametro(Constants.BANDWITDH_ID);
            bandwidthParam.setValor(bandwidth);
            // Packet Loss
            QoSParam packetLossParam = new QoSParam();
            packetLossParam.setCodigoParametro(Constants.PACKET_LOSS_ID);
            packetLossParam.setValor(pingResults.getPacketloss());
            // Jitter
            QoSParam jitterParam = new QoSParam();
            jitterParam.setCodigoParametro(Constants.JITTER_ID);
            jitterParam.setValor(pingResults.getRttMax() - pingResults.getRttMin());

            // Se agregan los parametros
            parametrosQos.add(delayParam);
            parametrosQos.add(bandwidthParam);
            parametrosQos.add(packetLossParam);
            parametrosQos.add(jitterParam);
            extras.putParcelableArrayList(Constants.EXTRA_PARAM_QOS, parametrosQos);

            intent.putExtras(extras);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

}
