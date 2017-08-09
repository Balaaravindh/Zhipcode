package com.falconnect.zipcode.MapModules;


import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public Double Start_Lat;
    public Double Start_Lng;

    public List<LatLng> points;
}