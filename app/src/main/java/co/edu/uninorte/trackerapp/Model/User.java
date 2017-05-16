package co.edu.uninorte.trackerapp.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by fdjvf on 5/8/2017.
 */
@IgnoreExtraProperties
public class User {

    public String Name;
    public String Imagen;
    public String UID;

    public ArrayList<Position> Route;

    public User(String name, String imagen, String UID) {

        Name = name;
        Imagen = imagen;
        this.UID = UID;
        Route = new ArrayList<>();

    }

    public User() {

    }



}
