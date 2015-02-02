package py.fpuna.tesis.qoetest.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by LF on 14/10/2014.
 */
public class DeviceStatusUtils {
    private BufferedReader reader;
    private Context context;

    public DeviceStatusUtils(Context context){
        this.context = context;
    }


    public int getBateryLevel(){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL , 0);
        return level;
    }
}
