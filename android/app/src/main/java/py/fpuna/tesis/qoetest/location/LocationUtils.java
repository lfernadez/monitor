package py.fpuna.tesis.qoetest.location;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;

import py.fpuna.tesis.qoetest.utils.Constants;

/**
 * Created by User on 02/10/2014.
 */
public class LocationUtils {

    private Context mContext;
    private LocationClientHelper mLocationClientHelper;


    public LocationUtils(Context mContext) {
        this.mContext = mContext;
        this.mLocationClientHelper = new LocationClientHelper(mContext);
    }

    public Location getLastLocation(){
        return this.mLocationClientHelper.getCurrentLocation();
    }
    public void connect(){
        this.mLocationClientHelper.connect();
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(Constants.INTERVAL_UPDATE_LOCATION_MILIS);
        locationRequest.setPriority(LocationRequest
                .PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(Constants
                .FAST_CEILING_IN_SECONDS);

        return locationRequest;
    }


}
