package py.fpuna.tesis.qoetest.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
    private int i;
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
                }else{
                    mCargaProm = ((i -1) /(i*mCargaAnterior)) + (carga/i);
                    mCargaAnterior = mCargaProm;
                }
                mHandler.postDelayed(monitoring,
                        Constants.TIEMPO_ACTUALIZACION);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
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


    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
