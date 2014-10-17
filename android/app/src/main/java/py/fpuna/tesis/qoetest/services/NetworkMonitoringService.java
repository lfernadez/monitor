package py.fpuna.tesis.qoetest.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetworkMonitoringService extends Service {

    private TelephonyManager telManager;
    private WifiManager wifiManager;

    private int signalLevelWiFi;
    private int signalLevel3G;
    private long dBm3G;

    private final IBinder mBinder = new NetworkServiceBinder();
    MyPhoneStateListener phoneStateListener;

    public NetworkMonitoringService() {
    }

    /**
     * @return
     */
    public int getSignalLevelWiFi() {
        return this.signalLevelWiFi;
    }

    /**
     * @return
     */
    public int getSignalLevel3G() {
        return this.signalLevel3G;
    }

    /**
     * @return
     */
    public long getdBm3G() {
        return this.dBm3G;
    }

    @Override
    public void onCreate() {
        wifiManager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);

        telManager = (TelephonyManager)
                getBaseContext().getSystemService(TELEPHONY_SERVICE);
        phoneStateListener = new MyPhoneStateListener();

    }

    public class NetworkServiceBinder extends Binder {
        public NetworkMonitoringService getService() {
            return NetworkMonitoringService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /* Broadcaste receiver WIFI*/
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final WifiManager wifiManager = (WifiManager) getSystemService
                        (Context.WIFI_SERVICE);
                int numberOfLevels = 5;
                if (wifiManager.isWifiEnabled()) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    signalLevelWiFi = WifiManager.calculateSignalLevel(wifiInfo
                            .getRssi(), numberOfLevels);
                    Log.d("SENAL WiFI", String.valueOf(signalLevelWiFi));
                }

            }
        }, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));

        telManager.listen(phoneStateListener, PhoneStateListener
                .LISTEN_SIGNAL_STRENGTHS);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            int signalStr = signalStrength.getGsmSignalStrength(); // ASU

            if (signalStr <= 2 || signalStr == 99) {
                signalLevel3G = 0;
            } else if (signalStr >= 12) {
                signalLevel3G = 4;
            } else if (signalStr >= 8) {
                signalLevel3G = 3;
            } else if (signalStr >= 5) {
                signalLevel3G = 2;
            } else {
                signalLevel3G = 1;
            }
            Log.d("SENAL ASU", String.valueOf(signalLevel3G));
            dBm3G = 2 * signalStr - 113;
            Log.d("SENAL ASU", String.valueOf(dBm3G));
        }
    }
}
