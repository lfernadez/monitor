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

import py.fpuna.tesis.qoetest.activity.PrincipalActivity;
import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.utils.Constants;


public class MonitoringService extends Service {

    public static final int NOTIF_ID = 101;
    Thread backThread;
    private ConnectivityManager cm;
    private WifiManager wifiManager;
    private TelephonyManager telManager;

    public MonitoringService() {
    }

    /**
     * Network Speed Monitor
     */
    private void monitoring() {
        long timeanterior = System.currentTimeMillis() / 1000;
        long bytesRXanterior = TrafficStats.getMobileRxBytes();
        long bytesWiFiRXAnterior = TrafficStats.getTotalRxBytes() - bytesRXanterior;
        long bytesTXanterior = TrafficStats.getMobileTxBytes();
        long bytesWiFiTXAnterior = TrafficStats.getTotalTxBytes() - bytesRXanterior;
        while (true) {
            try {
                long timeActual = System.currentTimeMillis() / 1000;
                long bytesRXActual = TrafficStats.getMobileRxBytes();
                long bytesTXActual = TrafficStats.getMobileTxBytes();
                long bytesWiFiRXActual = TrafficStats.getTotalRxBytes() - bytesRXActual;
                long bytesWiFiTXActual = TrafficStats.getTotalTxBytes() - bytesTXActual;
                long megaBytesDisponibles = getMemFree();
                long cpuLoad = getCpuLoad();

                /* Datos de la red activa */
                if (cm.getActiveNetworkInfo() != null) {
                    // Tipo de Red Mobile (3G, HSPA, HSPA+,...)
                    if (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE) {
                        long kbpsDOWN = ((bytesRXActual - bytesRXanterior) / 1000)
                                / (timeActual - timeanterior);
                        long kbpsUP = ((bytesTXActual - bytesTXanterior) / 1000)
                                / (timeActual - timeanterior);
                        updateNotification("UP: " + String.valueOf(kbpsUP) + "KB/s"
                                + "   DOWN: " + String.valueOf(kbpsDOWN) + "KB/s"
                                + " Mem Free: " + String.valueOf(megaBytesDisponibles)
                                + " MB CPU: " + String.valueOf(cpuLoad) + " %");
                        // Tipo de Red WiFi
                    } else if (cm.getActiveNetworkInfo().getType() ==
                            ConnectivityManager.TYPE_WIFI) {
                        long kbpsDOWNWiFi = ((bytesWiFiRXActual - bytesWiFiRXAnterior)
                                / 1000) / (timeActual - timeanterior);
                        long kbpsUPWiFi = ((bytesWiFiTXActual - bytesWiFiTXAnterior)
                                / 1000) / (timeActual - timeanterior);
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        if (wifiInfo != null) {
                            Integer linkSpeed = wifiInfo.getLinkSpeed(); //measured using WifiInfo.LINK_SPEED_UNITS
                            updateNotification("UP: " + String.valueOf(kbpsUPWiFi)
                                    + "KB/s DOWN: " + String.valueOf(kbpsDOWNWiFi)
                                    + "KB/s Mem Free: " + String.valueOf(megaBytesDisponibles)
                                    + " MB CPU: " + String.valueOf(cpuLoad) + " %");
                        }
                    }
                    // No hay red disponible
                } else {
                    updateNotification("Red no disponible, Mem Free: "
                            + String.valueOf(megaBytesDisponibles) + " MB CPU: "
                            + String.valueOf(cpuLoad) + " %");
                }
                Thread.sleep(250); // El hilo se duerme 250ms
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
        p.destroy();  // Se destruye el proceso creado
        return carga;
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
        backThread = new Thread(new Runnable() {
            @Override
            public void run() {
                monitoring();
            }
        });
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
        if (!backThread.isAlive()) {
            backThread.start();
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
