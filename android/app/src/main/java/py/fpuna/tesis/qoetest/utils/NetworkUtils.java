package py.fpuna.tesis.qoetest.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 15/09/2014.
 */
public class NetworkUtils {
    private Context context;
    TelephonyManager telManager;
    ConnectivityManager cm;

    public static long toKbits(long bytes){
        return (bytes *8)/1000;
    }

    /**
     *
     * @param context
     */
    public NetworkUtils(Context context) {
        this.context = context;
        telManager =
                (TelephonyManager) context.getSystemService(Context
                        .TELEPHONY_SERVICE);
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * @return
     */
    public String getOperatorName() {
        return telManager.getSimOperatorName();

    }

    /**
     * @return
     */
    public String getActiveNetworkType() {
        String networkType = "N/A";

        /* Datos de la red activa */
        if (cm.getActiveNetworkInfo() != null) {
            if (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE) {
                networkType = cm.getActiveNetworkInfo()
                        .getSubtypeName();
            } else if (cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI) {
                networkType = "WiFi";
            }
        }
        return networkType;
    }

    /**
     * @return
     */
    public List<Map<String, String>> getAllNetworkState() {
        List<Map<String, String>> allNetworksState = new ArrayList<Map<String,
                String>>();
        Map<String, String> item = new HashMap<String, String>();
        NetworkInfo [] networksInfo = cm.getAllNetworkInfo();
        for (NetworkInfo netInfo : networksInfo) {
            switch (netInfo.getType()) {
                case ConnectivityManager.TYPE_MOBILE:
                    item = new HashMap<String, String>();
                    item.put(netInfo.getSubtypeName(),
                            netInfo.getState().toString());
                    allNetworksState.add(item);
                    break;
                case ConnectivityManager.TYPE_WIFI:
                    item = new HashMap<String, String>();
                    item.put("WiFi",
                            netInfo.getState().toString());
                    allNetworksState.add(item);
                    break;
            }
        }
        return allNetworksState;
    }

    /**
     *
     * @return
     */
    public int getCID(){
        GsmCellLocation location = (GsmCellLocation)telManager.getCellLocation();
        int locationCellid = location.getCid();
        int cellId = -1;  //-1, desconocido por defecto
        if(cellId > 0) { // known location
            cellId = locationCellid & 0xffff; // get only valuable bytes
        }
        return cellId;
    }
}
