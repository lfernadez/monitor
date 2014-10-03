package py.fpuna.tesis.qoetest.location;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by User on 02/10/2014.
 */
public class LocationClientHelper implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private LocationClient mLocationClient;
    private Context mContext;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private PendingIntent mPendingIntent;
    private LocationRequest mLocationRequest;

    public enum RequestType {
        START, STOP, LAST;
    }

    private RequestType mRequestType;

    public LocationClientHelper(Context mContext) {
        this.mContext = mContext;
        mRequestType = null;
        mPendingIntent = null;
        mLocationRequest = null;
        mLocationClient = null;
    }

    private void stopPeriodicUpdates() {
    }

    private void startPeriodicUpdates() {

    }

    private void getLastLocation() {
        Location localizacionActual = getLocationClient().getLastLocation();
        sendLastLocation(localizacionActual);
    }

    private void sendLastLocation(Location location) {
        try {
            Intent intent = new Intent();
            intent.putExtra(LocationClient.KEY_LOCATION_CHANGED, location);
            mPendingIntent.send(mContext, Activity.RESULT_OK, intent);
        } catch (PendingIntent.CanceledException e) {
            //e.printStackTrace();
        }
    }

    public void startPeriodicUpdates(PendingIntent pendingIntent,
                                     LocationRequest locationRequest) {
        mRequestType = RequestType.START;
        mPendingIntent = pendingIntent;
        mLocationRequest = locationRequest;
        getLocationClient().connect();
    }

    public void getLastLocation(PendingIntent pendingIntent) {
        mRequestType = RequestType.LAST;
        mPendingIntent = pendingIntent;
        getLocationClient().connect();
    }

    public Location getCurrentLocation(){
        Location currentLocation = null;
        if(getLocationClient().isConnected()) {
            currentLocation = getLocationClient().getLastLocation();
        }
        return currentLocation;
    }


    /**
     * @return
     */
    public LocationClient getLocationClient() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(mContext,
                    this, this);
        }
        return mLocationClient;
    }

    /**
     * @param errorCode
     */
    private void showErrorDialog(int errorCode) {
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
                (Activity) mContext, CONNECTION_FAILURE_RESOLUTION_REQUEST);
        if (errorDialog != null) {
            errorDialog.show();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        /*switch (mRequestType) {
            case START:
                startPeriodicUpdates();
                break;
            case STOP:
                stopPeriodicUpdates();
                break;
            case LAST:
                getLastLocation();
                break;
            default:
                break;
        }*/
        //getLocationClient().disconnect();
    }

    @Override
    public void onDisconnected() {
        mLocationClient = null;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mContext instanceof Activity || mContext instanceof FragmentActivity) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult((Activity)
                            mContext, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                } catch (IntentSender.SendIntentException e) {
                    //e.printStackTrace();
                }
            } else {
                showErrorDialog(connectionResult.getErrorCode());
            }
        }
    }

    public void connect(){
        getLocationClient().connect();
    }


}
