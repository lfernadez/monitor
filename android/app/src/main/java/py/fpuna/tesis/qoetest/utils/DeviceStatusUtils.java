package py.fpuna.tesis.qoetest.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

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

    public float getLoadAvg(){
        RandomAccessFile reader = null;
        try {
            reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();
            String avgs [] = load.split(" ");
            return Float.valueOf(avgs[0]);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;

    }

}
