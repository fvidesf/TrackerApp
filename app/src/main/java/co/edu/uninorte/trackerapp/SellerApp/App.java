package co.edu.uninorte.trackerapp.SellerApp;

import android.app.Application;

/**
 * Created by fdjvf on 5/15/2017.
 */

public class App extends Application {

    private static App mInstance;
    private GoogleApiHelper googleApiHelper;

    public static synchronized App getInstance() {
        return mInstance;
    }

    public static GoogleApiHelper getGoogleApiHelper() {

        return getInstance().getGoogleApiHelperInstance();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        googleApiHelper = new GoogleApiHelper(getApplicationContext());
    }

    public GoogleApiHelper getGoogleApiHelperInstance() {
        return this.googleApiHelper;
    }
}
