package py.fpuna.tesis.qoetest.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import py.fpuna.tesis.qoetest.utils.Constants;

/**
 *
 */
public class CPUMonitoringService extends Service {
    private BufferedReader reader;
    HandlerThread mHandlerThread;
    private Handler mHandler;
    private double mCargaProm;
    private double mCargaAnterior;
    private int mCargaActual;
    private float tmp;
    private int i;
    private long idle1;
    private long cpu1;
    private final IBinder mBinder = new LocalBinder();


    private Runnable monitoring = new Runnable() {
        @Override
        public void run() {
            double carga = 0;
            // Llamada al comando top desde un proceso
            Process p = null;
            try {
                p = Runtime.getRuntime().exec("/system/bin/top  -m 1 -d 1 -n 1");
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
                i++;
                if(i == 1){
                    mCargaProm = carga;
                    mCargaAnterior = Math.log(mCargaProm);
                }else{
                    mCargaProm = (((i -1) * mCargaAnterior) + Math.log(carga))/i;
                    mCargaAnterior = mCargaProm;
                }
                Log.d("CPU Load", String.valueOf(carga));
                Log.d("CPU Prom", String.valueOf(Math.exp(mCargaProm)));
                mHandler.postDelayed(monitoring,
                        Constants.TIEMPO_ACTUALIZACION);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    private Runnable cpuUsageMonitoring = new Runnable() {
        @Override
        public void run() {
            try {
                RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
                String load = reader.readLine();

                String[] toks = load.split(" +");  // Split on one or more spaces

                long idle2 = Long.parseLong(toks[4]);
                long cpu2 = Long.parseLong(toks[1]) + Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                        + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
                if(idle1 != 0 && cpu1 != 0) {
                    tmp = (float) (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));
                    mCargaActual = Math.round(tmp * 100);
                }
                idle1 = idle2;
                cpu1 = cpu2;

                Log.d("CPU Uso",String.valueOf(mCargaActual));
                reader.close();

                mHandler.postDelayed(cpuUsageMonitoring,
                        Constants.TIEMPO_ACTUALIZACION);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    };

    @Override
    public void onCreate() {
        i = 0;
        mCargaProm = 0;
        mCargaAnterior = 0;
        mHandlerThread = new HandlerThread("CPUMonitoringThread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        mHandler.postDelayed(cpuUsageMonitoring, 15000);
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


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public double getLoadAverage() {
        return mCargaProm;
    }

    public void removeThread() {
        mHandler.removeCallbacks(monitoring);
    }

    /**
     *
     */
    public class LocalBinder extends Binder {
        public CPUMonitoringService getService() {
            return CPUMonitoringService.this;
        }
    }
}
