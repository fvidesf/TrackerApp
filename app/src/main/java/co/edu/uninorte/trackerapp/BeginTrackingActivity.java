package co.edu.uninorte.trackerapp;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import co.edu.uninorte.trackerapp.databinding.BeginTrackingBinding;


public class BeginTrackingActivity extends AppCompatActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {


    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 6000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    protected static final String TAG = "TrackingActivity";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;
    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected Boolean mRequestingLocationUpdates;
    Route newroute;
    int i = 1;
    private DatabaseReference RouteCollection;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        BeginTrackingBinding beginTrackingBinding = DataBindingUtil.setContentView(this, R.layout.activity_begin_tracking);
        mRequestingLocationUpdates = false;
        buildGoogleApiClient();
        createLocationRequest();
        RouteCollection = FirebaseDatabase.getInstance().getReference("Rutas");
        buildLocationSettingsRequest();

    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {


        if (mRequestingLocationUpdates) {
            Log.i(TAG, "in onConnected(), starting location updates");
            startLocationUpdates();
        }


    }


    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {

        newroute.getRoute().add(new Position(location.getLatitude(), location.getLongitude()));

    }

    /**
     * The callback invoked when
     * <p>
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * <p>
     * LocationSettingsRequest)} is called. Examines the
     * <p>
     * {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     * <p>
     * location settings are adequate. If they are not, begins the process of presenting a location
     * <p>
     * settings dialog to the user.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            // Check for the integer request code originally supplied to startResolutionForResult().

            case REQUEST_CHECK_SETTINGS:

                switch (resultCode) {

                    case Activity.RESULT_OK:

                        Log.i(TAG, "User agreed to make required location settings changes.");

                        break;

                    case Activity.RESULT_CANCELED:

                        Log.i(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;

                }

                break;

        }

    }


    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * <p>
     * updates have already been requested.
     */
    public void BeginLocationTrackingOnClick(View view) {

        if (!mRequestingLocationUpdates) {

            mRequestingLocationUpdates = true;
            newroute = new Route(new ArrayList<Position>(), "Ruta " + i, new Date());
            startLocationUpdates();
            i++;

            //Crear nueva ruta
        }

    }


    /**
     * Handles the Stop Updates button, and requests removal of location updates.
     */
    public void EndLocationTrackingOnClick(View view) {

        //Guardar rut
        RouteCollection.push().setValue(newroute);
        stopLocationUpdates();

    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {

        LocationServices.SettingsApi.checkLocationSettings(
                mGoogleApiClient,
                mLocationSettingsRequest
        ).setResultCallback(new ResultCallback<LocationSettingsResult>() {

            @Override

            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.i(TAG, "All location settings are satisfied.");

                        if (ActivityCompat.checkSelfPermission(BeginTrackingActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, BeginTrackingActivity.this);

                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +

                                "location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            status.startResolutionForResult(BeginTrackingActivity.this, REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {

                            Log.i(TAG, "PendingIntent unable to execute request.");

                        }

                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        String errorMessage = "Location settings are inadequate, and cannot be " +

                                "fixed here. Fix in Settings.";

                        Log.e(TAG, errorMessage);

                        Toast.makeText(BeginTrackingActivity.this, errorMessage, Toast.LENGTH_LONG).show();

                        mRequestingLocationUpdates = false;

                }


            }

        });


    }


    //region Codigo importante no tocar

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * <p>
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * <p>
     * if a device has the needed location settings.
     */
    protected void buildLocationSettingsRequest() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        builder.addLocationRequest(mLocationRequest);

        mLocationSettingsRequest = builder.build();

    }


    /**
     * Sets up the location request. Android has two location request settings:
     * <p>
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * <p>
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * <p>
     * the AndroidManifest.xml.
     * <p>
     * <p/>
     * <p>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * <p>
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * <p>
     * accurate to within a few feet.
     * <p>
     * <p/>
     * <p>
     * These settings are appropriate for mapping applications that show real-time location
     * <p>
     * updates.
     */

    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * <p>
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {

        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */

    protected void stopLocationUpdates() {

        // It is a good practice to remove location requests when the activity is in a paused or

        // stopped state. Doing so helps battery performance and is especially

        // recommended in applications that request frequent location updates.

        LocationServices.FusedLocationApi.removeLocationUpdates(

                mGoogleApiClient,

                this

        ).setResultCallback(new ResultCallback<Status>() {

            @Override

            public void onResult(Status status) {

                mRequestingLocationUpdates = false;
            }

        });

    }

    @Override
    protected void onStart() {

        super.onStart();

        mGoogleApiClient.connect();

    }

    @Override

    public void onResume() {

        super.onResume();

        // Within {@code onPause()}, we pause location updates, but leave the

        // connection to GoogleApiClient intact.  Here, we resume receiving

        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {

            startLocationUpdates();

        }
    }


    @Override

    protected void onPause() {

        super.onPause();

        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.

        if (mGoogleApiClient.isConnected()) {

            stopLocationUpdates();

        }

    }


    @Override

    protected void onStop() {

        super.onStop();

        mGoogleApiClient.disconnect();

    }


    @Override

    public void onConnectionSuspended(int cause) {

        Log.i(TAG, "Connection suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in

        // onConnectionFailed.

        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());

    }
    //endregion


}