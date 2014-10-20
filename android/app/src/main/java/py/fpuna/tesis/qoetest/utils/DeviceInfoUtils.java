package py.fpuna.tesis.qoetest.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Pattern;

import py.fpuna.tesis.qoetest.model.PhoneInfo;

/**
 * Created by User on 15/09/2014.
 */
public class DeviceInfoUtils {

    Context context;

    public DeviceInfoUtils(Context context){
        this.context = context;
    }

    /**
     * Obtiene el modelo del dispositivo
     * @return
     */
    public String getDeviceModel(){
        return Build.MODEL;
    }

    /**
     * Obtiene el vendedor del dispositivo
     * @return
     */
    public String getManufacturer(){
        return Build.MANUFACTURER;
    }

    /**
     *
     * @return
     */
    public String getScreenSize(){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        String screenSize = String.valueOf(dm.heightPixels) + "x" + String
                .valueOf(dm.widthPixels);
        return screenSize;
    }

    /**
     *
     * @return
     */
    public String getIMEI(){
        TelephonyManager telephonyManager =
                (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     *
     * @return
     */
    public long getRAM(){
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        return memInfo.totalMem / Constants.MULTIPLO_MB;
    }

    public long getRAMProc(){
        long totalRam = 0;
        String ram = "";
        try{
            RandomAccessFile reader = new RandomAccessFile("/proc/meminfo","r");
            String line = reader.readLine();
            ram = line.replaceAll("\\D+","");
            totalRam = Long.valueOf(ram);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return totalRam / Constants.MULTIPLI_KB_MB;
    }

    /**
     *
     * @return
     */
    public String getOSVersion(){
        return "ANDROID " + Build.VERSION.RELEASE;
    }

    /**
     *
     * @return
     */
    public PhoneInfo getPhoneInfo(){
        PhoneInfo info = new PhoneInfo();
        info.setModelo(getDeviceModel());
        info.setMarca(getManufacturer());
        info.setCpuCores(getCores());
        info.setCpuFrec(getMaxCPUFreqMHz());
        info.setProcesador(getProcessor());
        info.setPantalla(getScreenSize());
        info.setSoVersion(getOSVersion());
        info.setImei(getIMEI());
        info.setRam(String.valueOf(getRAMProc()) + " MB");
        return info;
    }

    /**
     *
     * @return
     */
    public int getCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            //Default to return 1 core
            return 1;
        }
    }

    /**
     *
     * @return
     */
    public int getMaxCPUFreqMHz() {

        int maxFreq = -1;
        try {

            RandomAccessFile reader = new RandomAccessFile("/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state", "r");

            boolean done = false;
            while (!done) {
                String line = reader.readLine();
                if (null == line) {
                    done = true;
                    break;
                }
                String[] splits = line.split("\\s+");
                assert (splits.length == 2);
                int timeInState = Integer.parseInt(splits[1]);
                if (timeInState > 0) {
                    int freq = Integer.parseInt(splits[0]) / 1000;
                    if (freq > maxFreq) {
                        maxFreq = freq;
                    }
                }
            }
            reader.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxFreq;
    }

    /**
     *
     * @return
     */
    public String getProcessor(){
        String processor = "Desconocido";
        try{
            RandomAccessFile reader = new RandomAccessFile("/proc/cpuinfo","r");
            String line = reader.readLine();
            while(!line.contains("Hardware")){
                line = reader.readLine();
            }
            processor = line.split("Hardware\t:")[1];
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return processor;
    }

}
