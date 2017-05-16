package co.edu.uninorte.trackerapp.SellerApp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import co.edu.uninorte.trackerapp.MainActivity;
import co.edu.uninorte.trackerapp.Model.User;
import co.edu.uninorte.trackerapp.R;
import co.edu.uninorte.trackerapp.databinding.BeginTrackingBinding;


public class BeginTrackingActivity extends AppCompatActivity {


    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    protected static final int REQUEST_PERMISSIONS = 89;
    protected static final String TAG = "TrackingActivity";
    protected Status status;
    DatabaseReference Vendedores;
    String ID;
    private User myUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        BeginTrackingBinding beginTrackingBinding = DataBindingUtil.setContentView(this, R.layout.activity_begin_tracking);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Vendedores = App.getSellers();
        myUser = new User(currentUser.getDisplayName(), currentUser.getPhotoUrl().toString(), currentUser.getUid());
        Vendedores.child(myUser.UID).setValue(myUser);
        final ImageView admin = beginTrackingBinding.imageView2;
        beginTrackingBinding.NameUser.setText(myUser.Name.toUpperCase());
        Picasso.with(this).load(currentUser.getPhotoUrl()).placeholder(R.drawable.com_facebook_auth_dialog_background).into(admin, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap imageBitmap = ((BitmapDrawable) admin.getDrawable()).getBitmap();
                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                imageDrawable.setCircular(true);
                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                admin.setImageDrawable(imageDrawable);
            }

            @Override
            public void onError() {
                admin.setImageResource(R.drawable.com_facebook_button_background);


            }
        });
    }

    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * <p>
     * updates have already been requested.
     */
    public void BeginLocationTrackingOnClick(View view) {

        RequestPermissions();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permisos Obtenidos", Toast.LENGTH_LONG).show();

                    //Si tengo permisos comenzar TRACKING
                    ServiceTracking();

                } else {
                    Toast.makeText(this, "No TIENES PERMISOS :C", Toast.LENGTH_LONG).show();
                    String[] permisos = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
                    ActivityCompat.requestPermissions(BeginTrackingActivity.this, permisos, REQUEST_PERMISSIONS);
                    return;
                }
            }
        }
    }

    public void ServiceTracking() {
        App.getGoogleApiHelper().locationServicesHelper.startLocationUpdates(App.getGoogleApiHelper().mGoogleApiClient);
        Intent service = new Intent(this, TrackingService.class);
        service.putExtra("ID", myUser.UID);
        startService(service);
        Toast.makeText(this, "Proceso de Tracking ha comenzado", Toast.LENGTH_LONG).show();

    }

    public void RequestPermissions() {
        LocationServices.SettingsApi.checkLocationSettings(
                App.getGoogleApiHelper().mGoogleApiClient, App.getGoogleApiHelper().locationServicesHelper.getmLocationSettingsRequest())
                .setResultCallback(new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                        status = locationSettingsResult.getStatus();
                        //  Log.d(TAG,"OnResult "+status.getStatusCode());
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:

                                if (ActivityCompat.checkSelfPermission(BeginTrackingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    Toast.makeText(BeginTrackingActivity.this, "No tiene permisos", Toast.LENGTH_LONG).show();
                                    String[] permisos = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
                                    ActivityCompat.requestPermissions(BeginTrackingActivity.this, permisos, REQUEST_PERMISSIONS);
                                    return;
                                }
                                ServiceTracking();

                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Toast.makeText(BeginTrackingActivity.this, "RESOLUTION_REQUIRED", Toast.LENGTH_LONG).show();
                                try {
                                    status.startResolutionForResult(BeginTrackingActivity.this, REQUEST_CHECK_SETTINGS);//Activar los servicios en el CELULAR
                                    //(pero todavia no ha pedido permisos para usarlos la app
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Toast.makeText(BeginTrackingActivity.this, "SETTINGS_CHANGE_UNAVAILABLE", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            // Check for the integer request code originally supplied to startResolutionForResult().

            case REQUEST_CHECK_SETTINGS:

                switch (resultCode) {

                    case Activity.RESULT_OK:

                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "No tiene permisos", Toast.LENGTH_LONG).show();
                            String[] permisos = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
                            ActivityCompat.requestPermissions(this, permisos, REQUEST_PERMISSIONS);
                            return;
                        }
                        ServiceTracking();
                        Log.i(TAG, "User agreed to make required location settings changes.");

                        break;

                    case Activity.RESULT_CANCELED:
                        try {
                            status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);//Activar los servicios en el CELULAR
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }

                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;

                }

                break;

        }
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates.
     */
    public void EndLocationTrackingOnClick(View view) {
        stopService(new Intent(this, TrackingService.class));
        Toast.makeText(this, "Tracking Terminado", Toast.LENGTH_LONG);
        finish();

    }
    public void LogOut(View view) {

        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        stopService(new Intent(BeginTrackingActivity.this, TrackingService.class));
                        Toast.makeText(BeginTrackingActivity.this, "Tracking Terminado", Toast.LENGTH_LONG);
                        startActivity(new Intent(BeginTrackingActivity.this, MainActivity.class));
                        finish();
                    }
                });

    }

    //endregion


}