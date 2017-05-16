package co.edu.uninorte.trackerapp.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by fdjvf on 5/7/2017.
 */
@IgnoreExtraProperties
public class Position {

    public String Latitude;
    public String Longitude;
    public Date RegisterDate;


    public Position() {


    }

    public Position(String Latitude, String Longitude) {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.RegisterDate = new Date();
    }
}
