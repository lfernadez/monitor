package py.fpuna.tesis.qoetest.utils;

import android.os.Build;

/**
 * Created by User on 15/09/2014.
 */
public class DeviceInfoUtils {

    /**
     * Obtiene el modelo del dispositivo
     * @return
     */
    public static String getDeviceModel(){
        return Build.MODEL;
    }

    /**
     * Obtiene el vendedor del dispositivo
     * @return
     */
    public static String getManufacturer(){
        return Build.MANUFACTURER;
    }

}
