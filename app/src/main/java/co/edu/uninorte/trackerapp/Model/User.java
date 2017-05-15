package co.edu.uninorte.trackerapp.Model;

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by fdjvf on 5/8/2017.
 */
@IgnoreExtraProperties
public class User {

    public String Name;
    public String Imagen;
    public ArrayList<Position> Route;

    public User(String name, Uri imagen) {

        Name = name;
        Imagen = imagen.toString();

    }

    public User() {

    }



}
