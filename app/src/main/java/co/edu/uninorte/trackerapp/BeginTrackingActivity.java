package co.edu.uninorte.trackerapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import co.edu.uninorte.trackerapp.databinding.BeginTrackingBinding;


public class BeginTrackingActivity extends AppCompatActivity {



    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        BeginTrackingBinding beginTrackingBinding = DataBindingUtil.setContentView(this, R.layout.activity_begin_tracking);

        try {
            PendingIntent myRequestIntent = (getIntent().getParcelableExtra("Resolution"));
            if (myRequestIntent != null) {
                startIntentSenderForResult(myRequestIntent.getIntentSender(), TrackingService.REQUEST_CHECK_SETTINGS, null, 0, 0, 0);
            }

        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }


    }




    /**
     * The callback invoked when
     * <p>
     *  is called. Examines the
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

            case TrackingService.REQUEST_CHECK_SETTINGS:

                switch (resultCode) {

                    case Activity.RESULT_OK:

                        Log.i(TrackingService.TAG, "User agreed to make required location settings changes.");
                        finish();
                        break;

                    case Activity.RESULT_CANCELED:

                        Log.i(TrackingService.TAG, "User chose not to make required location settings changes.");

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

        Intent service = new Intent(this, TrackingService.class);

        startService(service);


    }


    /**
     * Handles the Stop Updates button, and requests removal of location updates.
     */
    public void EndLocationTrackingOnClick(View view) {

        //Guardar rut
    /*    RouteCollection.push().setValue(newroute);
        stopLocationUpdates();*/

    }




    public void LogOut(View view) {

        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(BeginTrackingActivity.this, MainActivity.class));
                        finish();
                    }
                });

    }
    //endregion


}