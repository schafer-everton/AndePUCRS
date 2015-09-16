package com.pucrs.andepucrs.controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.directions.route.Route;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.directions.route.Segment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pucrs.andepucrs.R;

import java.util.List;

public class MapsActivityTESTE extends FragmentActivity implements RoutingListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final LatLng LOWER_MANHATTAN = new LatLng(-30.059794,
            -51.1733438);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(-30.0599683, -51.1714104);
    private static final LatLng WALL_STREET = new LatLng(-30.061237, -51.172944);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_activity_teste);
        setUpMapIfNeeded();

        LatLng start = LOWER_MANHATTAN;
       // LatLng waypoint= BROOKLYN_BRIDGE;
        LatLng end = WALL_STREET;

        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.WALKING)
                .withListener(this)
                .waypoints(start, end)
                .build();
        routing.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onRoutingFailure() {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(PolylineOptions polylineOptions, Route route) {
        PolylineOptions polyoptions = new PolylineOptions();
        polyoptions.color(Color.BLUE);
        polyoptions.width(10);
        polyoptions.addAll(polylineOptions.getPoints());
        mMap.addPolyline(polyoptions);
        List<Segment> s = route.getSegments();

        Log.i("Teste",s.get(0).getInstruction());
    }

    @Override
    public void onRoutingCancelled() {

    }
}
