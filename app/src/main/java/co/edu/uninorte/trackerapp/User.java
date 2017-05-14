package co.edu.uninorte.trackerapp;

import java.util.ArrayList;

/**
 * Created by fdjvf on 5/8/2017.
 */

public class User {
    String Name;
    ArrayList<Position> Route;

    public User(String name, String roleValue, ArrayList<Position> route) {
        Name = name;
        Route = route;
    }


    public User() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<Position> getRoute() {
        return Route;
    }

    public void setRoute(ArrayList<Position> route) {
        Route = route;
    }

}
