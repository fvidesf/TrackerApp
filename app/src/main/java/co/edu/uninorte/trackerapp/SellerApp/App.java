package co.edu.uninorte.trackerapp.SellerApp;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by fdjvf on 5/15/2017.
 */

public class App extends Application {

    private static App mInstance;
    private GoogleApiHelper googleApiHelper;
    private DatabaseReference Vendedores;
    private DatabaseReference connectedRef;
    public static synchronized App getInstance() {
        return mInstance;
    }

    public static GoogleApiHelper getGoogleApiHelper() {

        return getInstance().getGoogleApiHelperInstance();

    }

    public static DatabaseReference getConnectedRef() {
        return getInstance().getConnectedRefInstance();
    }

    public static DatabaseReference getSellers() {
        return getInstance().getVendedoresInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        googleApiHelper = new GoogleApiHelper(getApplicationContext());
        Vendedores = FirebaseDatabase.getInstance().getReference("Vendedores");
        Vendedores.keepSynced(true);
        connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
    }

    public DatabaseReference getConnectedRefInstance() {
        return this.connectedRef;
    }

    public DatabaseReference getVendedoresInstance() {
        return this.Vendedores;
    }

    public GoogleApiHelper getGoogleApiHelperInstance() {
        return this.googleApiHelper;
    }
}
