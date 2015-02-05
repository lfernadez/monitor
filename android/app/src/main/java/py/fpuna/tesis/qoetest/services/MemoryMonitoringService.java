package py.fpuna.tesis.qoetest.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import py.fpuna.tesis.qoetest.utils.Constants;
import py.fpuna.tesis.qoetest.utils.DeviceInfoUtils;

/**
 *
 */
public class MemoryMonitoringService extends Service {
    HandlerThread mHandlerThread;
    private Handler mHandler;
    private float totalRAM;
    private final IBinder mBinder = new ServiceBinder();
    private DeviceInfoUtils infoUtils;
    private long ramUsagePorcentaje;
    private float ramUsage;
    private double mCargaProm;
    private double mCargaAnterior;
    private int i;
    private long availableMegs;

    WindowManager windowManager;
    TextView text;
    private boolean agregado;

    private Runnable monitoring = new Runnable() {
        @Override
        public void run() {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(mi);
            availableMegs = mi.availMem / Constants.MULTIPLO_MB;
            ramUsage = totalRAM - availableMegs;
            ramUsagePorcentaje = Math.round((ramUsage / totalRAM) * 100);
            i++;
            if(i == 1){
                mCargaProm = ramUsage;
                mCargaAnterior = mCargaProm;
            }else{
                mCargaProm = (((i -1) * mCargaAnterior) + ramUsage)/i;
                mCargaAnterior = mCargaProm;
            }
            updateLoad(ramUsagePorcentaje);
            Log.d("Memory Usage", String.valueOf(ramUsage));
            Log.d("Memory Usage %:",String.valueOf(ramUsagePorcentaje));
            Log.d("RAM Usage Average", String.valueOf(mCargaProm));
            mHandler.postDelayed(monitoring, Constants.TIEMPO_ACTUALIZACION);
        }
    };

    @Override
    public void onCreate() {
        i = 0;
        infoUtils = new DeviceInfoUtils(getApplicationContext());
        totalRAM = infoUtils.getRAMProc();
        text = new TextView(this);
        agregado = false;
        text.setTextColor(Color.WHITE);
        text.setShadowLayer(1, 0, 0, Color.BLACK);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
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

    public class ServiceBinder extends Binder {
        public MemoryMonitoringService getService() {
            return MemoryMonitoringService.this;
        }

    }

    public void updateLoad(long newValue) {
        text.setText("Mem: " + String.valueOf(newValue) + "%");
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.RIGHT | Gravity.TOP;
        params.verticalMargin = 1;
        if (agregado) {
            windowManager.updateViewLayout(text, params);
        } else {
            windowManager.addView(text, params);
            agregado = true;
        }
    }
}
