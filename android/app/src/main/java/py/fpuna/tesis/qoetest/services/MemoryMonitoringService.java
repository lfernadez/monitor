package py.fpuna.tesis.qoetest.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import py.fpuna.tesis.qoetest.utils.Constants;
import py.fpuna.tesis.qoetest.utils.DeviceInfoUtils;

/**
 *
 */
public class MemoryMonitoringService extends Service {
    HandlerThread mHandlerThread;
    private Handler mHandler;
    private float totalRAM;
    private final IBinder mBinder = new LocalBinder();
    private DeviceInfoUtils infoUtils;
    private long ramUsage;
    private double mCargaProm;
    private double mCargaAnterior;
    private int i;
    private float availableMegs;

    private Runnable monitoring = new Runnable() {
        @Override
        public void run() {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(mi);
            availableMegs = mi.availMem / Constants.MULTIPLO_MB;
            ramUsage = Math.round(((totalRAM - availableMegs) / totalRAM) * 100);
            i++;
            if(i == 1){
                mCargaProm = ramUsage;
                mCargaAnterior = Math.log(mCargaProm);
            }else{
                mCargaProm = (((i -1) * mCargaAnterior) + Math.log(ramUsage))/i;
                mCargaAnterior = mCargaProm;
            }

            Log.d("Memory Usage %:",String.valueOf(ramUsage));
            Log.d("RAM Usage Average", String.valueOf(Math.exp(mCargaProm)));
            mHandler.postDelayed(monitoring, Constants.TIEMPO_ACTUALIZACION);
        }
    };

    @Override
    public void onCreate() {
        i = 0;
        infoUtils = new DeviceInfoUtils(getApplicationContext());
        totalRAM = infoUtils.getRAMProc();
        mHandlerThread = new HandlerThread("MemoryMonitoringThread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        mHandler.postDelayed(monitoring, 15000);
    }

    /**
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public double getLoadAverage() {
        return mCargaProm;
    }

    public void removeThread() {
        mHandler.removeCallbacks(monitoring);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public MemoryMonitoringService getService() {
            return MemoryMonitoringService.this;
        }

    }


}
