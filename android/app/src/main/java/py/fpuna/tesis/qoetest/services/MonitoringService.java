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

import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.activity.PrincipalActivity;
import py.fpuna.tesis.qoetest.model.PingResults;
import py.fpuna.tesis.qoetest.utils.Constants;
import py.fpuna.tesis.qoetest.utils.DateHourUtils;


public class MonitoringService extends Service {

    public static final int NOTIF_ID = 101;
    private ConnectivityManager cm;
    private WifiManager wifiManager;
    private TelephonyManager telManager;
    Thread mThread;

    public MonitoringService() {
    }

    /**
     * Network Speed Monitor
     */
    private void monitoring() {
        long timeanterior = DateHourUtils.toSeconds(System.currentTimeMillis());
        long bytesRXanterior = TrafficStats.getMobileRxBytes();
        long packetsRXanterior = TrafficStats.getMobileRxPackets();
        long packetsTXanterior = TrafficStats.getMobileTxPackets();
        long bytesWiFiRXAnterior = TrafficStats.getTotalRxBytes() - bytesRXanterior;
        long bytesTXanterior = TrafficStats.getMobileTxBytes();
        long bytesWiFiTXAnterior = TrafficStats.getTotalTxBytes() - bytesRXanterior;
        long packetsWiFiRXanterior = TrafficStats.getTotalRxPackets() - packetsRXanterior;
        long packetsWiFiTXanterior = TrafficStats.getTotalTxPackets() - packetsTXanterior;

        while (true) {
            try {
                long timeActual = DateHourUtils.toSeconds(System.currentTimeMillis());
                long bytesRXActual = TrafficStats.getMobileRxBytes();
                long bytesTXActual = TrafficStats.getMobileTxBytes();
                long bytesWiFiRXActual = TrafficStats.getTotalRxBytes() - bytesRXActual;
                long bytesWiFiTXActual = TrafficStats.getTotalTxBytes() - bytesTXActual;
                long packetsRXActual = TrafficStats.getMobileRxPackets();
                long packetsTXActual = TrafficStats.getMobileTxPackets();
                long packetsWiFiRXActual = TrafficStats.getTotalRxPackets() -
                        packetsRXActual;
                long packetsWiFiTXActual = TrafficStats.getTotalTxPackets() -
                        packetsTXActual;
                long megaBytesDisponibles = getMemFree();
                long cpuLoad = getCpuLoad();
                //PingResults pingResults = executePing();
                /* Datos de la red activa */
                if (cm.getActiveNetworkInfo() != null) {
                    // Tipo de Red Mobile (3G, HSPA, HSPA+,...)
                    if (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE) {
                        long kbpsDOWN = (((bytesRXActual - bytesRXanterior) *
                                8) / 1000) / (timeActual - timeanterior);
                        long kbpsUP = (((bytesTXActual - bytesTXanterior) * 8) / 1000) / (timeActual - timeanterior);
                        updateNotification("UP: " + String.valueOf(kbpsUP) +
                                "Kbps   DOWN: " + String.valueOf(kbpsDOWN) +
                                "Kbps Mem Free: " + String.valueOf(megaBytesDisponibles)
                                + " MB CPU: " + String.valueOf(cpuLoad) + "%");
                        // Tipo de Red WiFi
                    } else if (cm.getActiveNetworkInfo().getType() ==
                            ConnectivityManager.TYPE_WIFI) {
                        long kbpsDOWNWiFi = (((bytesWiFiRXActual -
                                bytesWiFiRXAnterior) * 8) / 1000) / (timeActual - timeanterior);
                        long kbpsUPWiFi = (((bytesWiFiTXActual -
                                bytesWiFiTXAnterior) * 8) / 1000) / (timeActual - timeanterior);
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        if (wifiInfo != null) {
                            Integer linkSpeed = wifiInfo.getLinkSpeed(); //measured using WifiInfo.LINK_SPEED_UNITS
                            updateNotification("Link speed " + String
                                    .valueOf(linkSpeed) + "Mbps UP: " + String
                                    .valueOf
                                            (kbpsUPWiFi)
                                    + "Kbps DOWN: " + String.valueOf(kbpsDOWNWiFi)
                                    + "Kbps Mem Free: " + String.valueOf(megaBytesDisponibles)
                                    + " MB CPU: " + String.valueOf(cpuLoad) +
                                    " %");
                        }
                    }
                    // No hay red disponible
                } else {
                    updateNotification("Red no disponible, Mem Free: "
                            + String.valueOf(megaBytesDisponibles) + " MB CPU: "
                            + String.valueOf(cpuLoad) + " %");
                }
                Thread.sleep(Constants.TIEMPO_ACTUALIZACION); // El hilo se duerme 250ms
                // Actualizacion de los valores anteriores
                timeanterior = timeActual;
                bytesRXanterior = bytesRXActual;
                bytesTXanterior = bytesTXActual;
                bytesWiFiRXAnterior = bytesWiFiRXActual;
                bytesWiFiTXAnterior = bytesWiFiTXActual;
            } catch (Exception e) {

            }
        }
    }


    /**
     * Obtiene la cantidad de memoria disponible en el sistema
     *
     * @return Memoria disponible en MB
     */
    private Long getMemFree() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / Constants.MULTIPLO_MB;
        return availableMegs;
    }

    /**
     * Obtiene la carga de la CPU dada a partir del comando top
     *
     * @return porcentaje de carga de la CPU
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    private long getCpuLoad() throws IOException, InterruptedException {
        long carga = 0;
        // Llamada al comando top desde un proceso
        Process p = Runtime.getRuntime().exec("/system/bin/top  -m 1 -d 1 -n 1");
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        // Parseo del resultado de top
        String line = reader.readLine();
        while ((line = reader.readLine()).isEmpty()) {
        }
        String[] porcentajes = line.split(", ");
        for (String item : porcentajes) {
            String[] porcentaje = item.split(" ");
            carga += Long.valueOf(porcentaje[1].substring(0,
                    porcentaje[1].length() - 1));
        }
        //reader.close();
        p.destroy();  // Se destruye el proceso creado
        return carga;
    }

    private PingResults executePing() throws IOException, InterruptedException {
        PingResults result = new PingResults();
        // Llamada al comando ping desde un proceso
        Process p = Runtime.getRuntime().exec("/system/bin/ping -a -c 5 " +
                "google.com");
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                p.getInputStream()));
        // Parseo del resultado de ping
        String line = reader.readLine();
        while (!(line = reader.readLine()).isEmpty()) {
        }
        reader.readLine();
        /* Parseo de las estadisticas */
        String[] estadisticas = reader.readLine().split(", ");
        result.setPacketsSended(Integer.valueOf(estadisticas[0].substring(0, 1)));
        result.setPacketsReceived(Integer.valueOf(estadisticas[1].substring(0, 1)));
        result.setPacketloss(Double.valueOf(estadisticas[2].substring(0, 1)));
        result.setTime(Long.valueOf(estadisticas[3].substring(5,
                estadisticas[3].length() - 2)));
        /* Parseo de los tiempo */
        line = reader.readLine();
        line = line.substring(line.lastIndexOf("=") + 2,
                line.length() - 3);
        String[] datosTiempos = line.split("/");
        result.setRttMin(Double.valueOf(datosTiempos[0]));
        result.setRttAvg(Double.valueOf(datosTiempos[1]));
        result.setRttMax(Double.valueOf(datosTiempos[2]));
        result.setRttStdDev(Double.valueOf(datosTiempos[3]));

        reader.close();
        return result;
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
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1,
                startIntent, 0);
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

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                monitoring();
            }
        }, "Monitoreo");

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
        if (!mThread.isAlive()) {
            mThread.start();
        }
        return START_STICKY;
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
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
