package net.xeraa.backend;

import javax.persistence.Embeddable;

@Embeddable
public class GeoPoint {
    private double lat;
    private double lon;

    public GeoPoint() {
    }

    public GeoPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public final double getLat() {
        return this.lat;
    }

    public final double getLon() {
        return this.lon;
    }

    public String toString() {
        return "[" + lat + ", " + lon + "]";
    }
}
