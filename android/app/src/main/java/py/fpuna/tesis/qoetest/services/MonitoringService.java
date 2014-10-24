package py.fpuna.tesis.qoetest.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.*;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Process;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.activity.PrincipalActivity;
import py.fpuna.tesis.qoetest.model.PingResults;
import py.fpuna.tesis.qoetest.utils.Constants;
import py.fpuna.tesis.qoetest.utils.DateHourUtils;
import py.fpuna.tesis.qoetest.utils.NetworkUtils;

public class MonitoringService extends Service {

    public static final int NOTIF_ID = 101;
    private ConnectivityManager cm;
    private WifiManager wifiManager;
    private TelephonyManager telManager;
    HandlerThread mHandlerThread;
    public static final String TAG = "MonitoringServive";

    private long bpsDown;
    private long bpsUP;
    private long cpuLoad;
    private long memLoad;

    /* Mobile network stats */
    private static long bytesRXanterior;
    private static long bytesRXActual;
    private static long packetsRXanterior;
    private static long packetsTXanterior;
    private static long packetsRXActual;
    private static long bytesTXanterior;
    private static long bytesTXActual;
    private static long packetsTXActual;

    /* WiFi network stats */
    private static long bytesWiFiRXAnterior;
    private static long bytesWiFiTXAnterior;
    private static long packetsWiFiRXanterior;
    private static long packetsWiFiTXanterior;
    private static long bytesWiFiRXActual;
    private static long bytesWiFiTXActual;
    private static long packetsWiFiRXActual;
    private static long packetsWiFiTXActual;

    private boolean startMean = false;
    private long timeAnterior;
    private long timeActual;

    /* Harmonic mean Down*/
    private static float hMBpsDown;

    /* Harmonic mean UP*/
    private static float hMBpsUP;

    private final IBinder mBinder = new LocalBinder();
    private BufferedReader reader;
    private Handler mHandler;

    public MonitoringService() {
    }

    /**
     *
     */
    public class LocalBinder extends Binder {
        public MonitoringService getService() {
            return MonitoringService.this;
        }
    }


    /**
     *
     */
    private Runnable monitoring = new Runnable() {
        @Override
        public void run() {
            timeActual = DateHourUtils.toSeconds(System.currentTimeMillis());
            bytesRXActual = TrafficStats.getMobileRxBytes();
            bytesTXActual = TrafficStats.getMobileTxBytes();
            bytesWiFiRXActual = TrafficStats.getTotalRxBytes() - bytesRXActual;
            bytesWiFiTXActual = TrafficStats.getTotalTxBytes() - bytesTXActual;
            packetsRXActual = TrafficStats.getMobileRxPackets();
            packetsTXActual = TrafficStats.getMobileTxPackets();
            packetsWiFiRXActual = TrafficStats.getTotalRxPackets() -
                    packetsRXActual;
            packetsWiFiTXActual = TrafficStats.getTotalTxPackets() -
                    packetsTXActual;
            //memLoad = getMemFree();
            //cpuLoad = getCpuLoad();
            //PingResults pingResults = executePing();
            /* Datos de la red activa */
            if (cm.getActiveNetworkInfo() != null) {
                // Tipo de Red Mobile (3G, HSPA, HSPA+,...)
                if (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE) {
                    long kbpsDOWN = (((bytesRXActual - bytesRXanterior) *
                            8) / 1000) / (timeActual - timeAnterior);
                    bpsDown = kbpsDOWN;
                    long kbpsUP = (((bytesTXActual - bytesTXanterior) * 8) / 1000) / (timeActual - timeAnterior);
                    bpsUP = kbpsUP;
                    Log.d(TAG, "UP: " + String.valueOf(kbpsUP) +
                            "Kbps   DOWN: " + String.valueOf(kbpsDOWN) +
                            "Kbps");
                /*updateNotification("UP: " + String.valueOf(kbpsUP) +
                        "Kbps   DOWN: " + String.valueOf(kbpsDOWN) +
                        "Kbps Mem Free: " + String.valueOf(megaBytesDisponibles)
                        + " MB CPU: " + String.valueOf(cpuLoad) + "%");*/
                    // Tipo de Red WiFi
                } else if (cm.getActiveNetworkInfo().getType() ==
                        ConnectivityManager.TYPE_WIFI) {
                    long kbpsDOWNWiFi = (((bytesWiFiRXActual -
                            bytesWiFiRXAnterior) * 8) / 1000) / (timeActual - timeAnterior);
                    bpsDown = kbpsDOWNWiFi;
                    long kbpsUPWiFi = (((bytesWiFiTXActual -
                            bytesWiFiTXAnterior) * 8) / 1000) / (timeActual - timeAnterior);
                    bpsUP = kbpsUPWiFi;
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null) {
                        Integer linkSpeed = wifiInfo.getLinkSpeed(); //measured using WifiInfo.LINK_SPEED_UNITS
                        Log.d(TAG, "Link speed " + String
                                .valueOf(linkSpeed) + "Mbps UP: " + String
                                .valueOf
                                        (kbpsUPWiFi)
                                + "Kbps DOWN: " + String.valueOf(kbpsDOWNWiFi)
                                + "Kbps");
                    /*updateNotification("Link speed " + String
                            .valueOf(linkSpeed) + "Mbps UP: " + String
                            .valueOf
                                    (kbpsUPWiFi)
                            + "Kbps DOWN: " + String.valueOf(kbpsDOWNWiFi)
                            + "Kbps Mem Free: " + String.valueOf(megaBytesDisponibles)
                            + " MB CPU: " + String.valueOf(cpuLoad) +
                            " %");*/
                    }
                }
                // No hay red disponible
            } else {
                bpsDown = 0;
                bpsUP = 0;
                Log.d(TAG, "Red no disponible");
            /*updateNotification("Red no disponible, Mem Free: "
                    + String.valueOf(megaBytesDisponibles) + " MB CPU: "
                    + String.valueOf(cpuLoad) + " %");*/
            }
            // Actualizacion de los valores anteriores
            timeAnterior = timeActual;
            bytesRXanterior = bytesRXActual;
            bytesTXanterior = bytesTXActual;
            bytesWiFiRXAnterior = bytesWiFiRXActual;
            bytesWiFiTXAnterior = bytesWiFiTXActual;
            mHandler.postDelayed(monitoring,
                    Constants.TIEMPO_ACTUALIZACION);
        }
    };

    /**
     * Obtiene la cantidad de memoria disponible en el sistema
     *
     * @return Memoria disponible en MB
     */
    public Double getMemFree() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / Constants.MULTIPLO_MB;
        return availableMegs;
    }

    public void startMean() {
        startMean = true;
    }

    /**
     * Obtiene la carga de la CPU dada a partir del comando top
     *
     * @return porcentaje de carga de la CPU
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    public double getCpuLoad() throws IOException, InterruptedException {
        double carga = 0;
        // Llamada al comando top desde un proceso
        Process p = Runtime.getRuntime().exec("/system/bin/top  -m 1 -d 1 -n 1");
        p.waitFor();
        reader = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        // Parseo del resultado de top
        String line = reader.readLine();
        while ((line = reader.readLine()).isEmpty()) {
        }
        String[] porcentajes = line.split(", ");
        for (String item : porcentajes) {
            String[] porcentaje = item.split(" ");
            carga += Double.valueOf(porcentaje[1].substring(0,
                    porcentaje[1].length() - 1));
        }
        reader.close();
        p.destroy();  // Se destruye el proceso creado
        return carga;
    }


    /**
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public PingResults executePing() throws IOException, InterruptedException {
        PingResults result = new PingResults();
        // Llamada al comando ping desde un proceso
        Process p = Runtime.getRuntime().exec("/system/bin/ping -a -c 5 " +
                Constants.IP_TRANSMITTER_SERVER);
        p.waitFor();
        reader = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        // Parseo del resultado de ping
        String line = reader.readLine();
        if (line != null) {
            while (!(line = reader.readLine()).isEmpty()) {
            }
            reader.readLine();
            /* Parseo de las estadisticas */
            line = reader.readLine();
            String[] estadisticas = line.split(", ");
            result.setPacketsSended(Integer.valueOf(estadisticas[0].replaceAll("\\D+","")));
            result.setPacketsReceived(Integer.valueOf(estadisticas[1].replaceAll("\\D+","")));
            result.setPacketloss(Double.valueOf(estadisticas[2].replaceAll("\\D+","")));
            result.setTime(Long.valueOf(estadisticas[3].replaceAll("\\D+","")));
            /* Parseo de los tiempo */
            line = reader.readLine();
            if (!line.isEmpty()) {
                line = line.substring(line.lastIndexOf("=") + 2,
                        line.length() - 3);
                String[] datosTiempos = line.replaceAll("[^\\d./]","").split("/");
                result.setRttMin(Double.valueOf(datosTiempos[0]));
                result.setRttAvg(Double.valueOf(datosTiempos[1]));
                result.setRttMax(Double.valueOf(datosTiempos[2]));
                result.setRttStdDev(Double.valueOf(datosTiempos[3]));
            }
        }
        reader.close();
        return result;
    }

    /**
     *
     * @return
     */
    public float getBandwidth() {
        long startTime = System.currentTimeMillis();
        float bandwidth = 0;
        InputStream input = null;

        try {
            URL url = new URL(Constants.IMAGE_URL_DOWN_DOS);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Cache-Control", "no-cache");
            long starTime = System.currentTimeMillis();
            connection.connect();
            int response = connection.getResponseCode();
            input = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(input);
            int red = 0;
            long size = 0;

            byte[] buf = new byte[1024];
            while ((red = bis.read(buf)) != -1) {
                size += red;
            }
            long endTime = System.currentTimeMillis();
            input.close();
            connection.disconnect();
            bandwidth = (NetworkUtils.toKbits(Constants.IMAGE_LENGTH))
                    / (DateHourUtils.toSeconds(endTime - startTime));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bandwidth;
    }


    /**
     * Actualiza la notificacion mostrada en pantalla
     *
     * @param datos
     */
    private void updateNotification(String datos) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Monitor")
                .setContentText(datos)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(datos));
        Intent startIntent = new Intent(getApplicationContext(),
                PrincipalActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1,startIntent, 0);
        builder.setContentIntent(contentIntent);
        Notification note = builder.build();
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIF_ID, note);
    }


    /**
     *
     */
    @Override
    public void onCreate() {
        cm = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        wifiManager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);

        telManager = (TelephonyManager)
                getBaseContext().getSystemService(TELEPHONY_SERVICE);
        init();
        mHandlerThread = new HandlerThread("MonitoringThread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        mHandler.postDelayed(monitoring, 15000);

        Toast.makeText(this, "Service Foreground", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIF_ID, getNotification());
        return START_STICKY;
    }

    /**
     *
     */
    public void init(){
        timeAnterior = DateHourUtils.toSeconds(System.currentTimeMillis());
        bytesRXanterior = TrafficStats.getMobileRxBytes();
        packetsRXanterior = TrafficStats.getMobileRxPackets();
        packetsTXanterior = TrafficStats.getMobileTxPackets();
        bytesWiFiRXAnterior = TrafficStats.getTotalRxBytes() - bytesRXanterior;
        bytesTXanterior = TrafficStats.getMobileTxBytes();
        bytesWiFiTXAnterior = TrafficStats.getTotalTxBytes() - bytesRXanterior;
        packetsWiFiRXanterior = TrafficStats.getTotalRxPackets() - packetsRXanterior;
        packetsWiFiTXanterior = TrafficStats.getTotalTxPackets() - packetsTXanterior;
    }

    /**
     * Crea una nueva notificacion
     *
     * @return notificacion
     */
    private Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Monitoring started");
        Intent startIntent = new Intent(getApplicationContext(),
                PrincipalActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1,
                startIntent, 0);
        builder.setContentIntent(contentIntent);
        Notification note = builder.build();
        return note;
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
        mHandler.removeCallbacks(monitoring);
        mHandler.getLooper().quitSafely();
    }

    /**
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * @return
     */
    public long getBpsDown() {
        return this.bpsDown;
    }

    /**
     * @return
     */
    public long getBpsUP() {
        return this.bpsUP;
    }

}
