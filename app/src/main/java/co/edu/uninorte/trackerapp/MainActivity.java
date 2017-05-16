package co.edu.uninorte.trackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import co.edu.uninorte.trackerapp.AdminApp.MainAdminActivity;
import co.edu.uninorte.trackerapp.SellerApp.BeginTrackingActivity;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final String ADMIN_MELANIS = "YQgiBDUx7qOFJHWkmR5WcvnT1s03";
    private static final String ADMIN_FABIO = "b2JACYw7QLUvDAhFj6K8zrHGeLa2";
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            SelectedIntentForUsers();
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder().
                            setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .setTheme(R.style.GreenTheme)
                            .build(), RC_SIGN_IN);
        }
    }
    public void SelectedIntentForUsers() {

        String UID = auth.getCurrentUser().getUid();

        if (UID.equals(ADMIN_MELANIS) || UID.equals(ADMIN_FABIO)) {
            Log.d("Adming Log", "Inicio sesion admin");
            Intent myIntent = new Intent(this, MainAdminActivity.class);
            startActivity(myIntent);
            //Iniciar Intent de Administrador
        } else {
            //Iniciar Intent de Vendedor
            Log.d("Adming Log", "Inicio sesion Vendedor");
            Intent myIntent = new Intent(this, BeginTrackingActivity.class);
            startActivity(myIntent);
        }
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == ResultCodes.OK || requestCode == RC_SIGN_IN) {
            SelectedIntentForUsers();
            finish();

        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                Toast.makeText(this, "El inicio de sesión ha sido cancelado", Toast.LENGTH_SHORT).show();

                return;
            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                Toast.makeText(this, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
                return;
            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                Toast.makeText(this, "Error desconocido", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }
}
