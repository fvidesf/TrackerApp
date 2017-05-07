package co.edu.uninorte.trackerapp;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by fdjvf on 5/7/2017.
 */

@IgnoreExtraProperties
public class Route {


    ArrayList<Position> route;
    String routeName;
    Date creationDate;

    public Route(ArrayList<Position> route, String routeName, Date creationDate) {
        this.route = route;
        this.routeName = routeName;
        this.creationDate = creationDate;
    }

    public Route() {

    }

    public ArrayList<Position> getRoute() {
        return route;
    }

    public void setRoute(ArrayList<Position> route) {
        this.route = route;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
