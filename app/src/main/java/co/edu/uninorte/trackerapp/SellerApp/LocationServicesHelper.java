package co.edu.uninorte.trackerapp.SellerApp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

/**
 * Created by fdjvf on 5/15/2017.
 */

public class LocationServicesHelper {

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 3000;
    private static final String TAG = LocationServicesHelper.class.getSimpleName();
    LocationListener locationListener;
    LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;

    public LocationServicesHelper(LocationListener locationListener) {

        this.locationListener = locationListener;
        createLocationRequest();
        buildLocationSettingsRequest();

    }

    public LocationSettingsRequest getmLocationSettingsRequest() {
        return mLocationSettingsRequest;
    }

    public void setmLocationSettingsRequest(LocationSettingsRequest mLocationSettingsRequest) {
        this.mLocationSettingsRequest = mLocationSettingsRequest;
    }

    public LocationRequest getmLocationRequest() {
        return mLocationRequest;
    }

    public void setmLocationRequest(LocationRequest mLocationRequest) {
        this.mLocationRequest = mLocationRequest;
    }

    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);


    }

    protected void buildLocationSettingsRequest() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
    }

    public void startLocationUpdates(GoogleApiClient mGoogleApiClient) {

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Log.d("GoogleApiHelper", "LocationBeging");
            if (ActivityCompat.checkSelfPermission(mGoogleApiClient.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener);

        }

    }


    public void stopLocationUpdates(GoogleApiClient mGoogleApiClient) {

        Log.d(TAG, "stopLocationUpdates");

        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {

            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationListener);

            mGoogleApiClient.disconnect();

        }

    }

}