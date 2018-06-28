package sg.redapp.com.redappdriver.Classes;


import java.io.Serializable;

/**
 * Created by angruixian on 27/6/18.
 */

public class AvailableDriver {

    public double latitude;
    public double longitude;

    public AvailableDriver() {
    }

    public AvailableDriver(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
