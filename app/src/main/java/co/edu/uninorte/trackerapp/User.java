package co.edu.uninorte.trackerapp;

import java.util.ArrayList;

/**
 * Created by fdjvf on 5/8/2017.
 */

public class User {
    String UID;
    ArrayList<Position> Route;

    public User(String name, ArrayList<Position> route) {
        UID = name;
        Route = route;
    }

    public User() {

    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public ArrayList<Position> getRoute() {
        return Route;
    }

    public void setRoute(ArrayList<Position> route) {
        Route = route;
    }


}
