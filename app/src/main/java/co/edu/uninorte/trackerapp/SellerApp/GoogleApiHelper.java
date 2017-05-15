package co.edu.uninorte.trackerapp.SellerApp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

/**
 * Created by fdjvf on 5/15/2017.
 */

public class GoogleApiHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "GoogleApiHelper";

    Context context;
    GoogleApiClient mGoogleApiClient;
    LocationServicesHelper locationServicesHelper;
    LocationListener locationListener = null;

    public GoogleApiHelper(Context context) {

        this.context = context;
        buildGoogleApiClient();
        checkLocationServices();
        connect();

    }

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void connect() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        Log.d(TAG, "onLocationChanged: location => " + location.getLongitude() + "," + location.getLatitude());

        if (locationListener != null) {

            Log.d(TAG, "Location listener forward");
            locationListener.onLocationChanged(location);

        }

    }

    public boolean isConnected() {

        if (mGoogleApiClient != null) {

            return mGoogleApiClient.isConnected();

        } else {

            return false;

        }

    }


    public void checkLocationServices() {


    }


    public Location getLastKnownLocation() {

        Location location = null;

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Something
            }
            location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        }
        return location;
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "API Conectada");
        locationServicesHelper = new LocationServicesHelper(this);
    }


    //region ImportanCode
    public void disconnect() {

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {

            mGoogleApiClient.disconnect();

        }

    }

    protected void buildGoogleApiClient() {

        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    public void addLocationLisetener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }

    public void removeLocationLisetener() {
        this.locationListener = null;
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.d(TAG, "onConnectionSuspended: googleApiClient.connect()");

        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed: connectionResult.toString() = " + connectionResult.toString());

    }

    //endregion


}