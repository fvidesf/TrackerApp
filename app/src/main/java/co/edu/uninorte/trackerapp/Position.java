package co.edu.uninorte.trackerapp;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by fdjvf on 5/7/2017.
 */
@IgnoreExtraProperties
public class Position {

    public Double Latitude;
    public Double Longitude;

    public Position() {

    }

    public Position(Double Latitude, Double Longitude) {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }
}
