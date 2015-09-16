package com.pucrs.andepucrs.controller;

import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pucrs.andepucrs.R;
import com.pucrs.andepucrs.api.Constants;
import com.pucrs.andepucrs.api.HttpConnection;
import com.pucrs.andepucrs.api.PathJSONParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivityBackup extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    SharedPreferences settings;
    MarkerOptions current;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Button markerButton;


    private static final LatLng LOWER_MANHATTAN = new LatLng(-30.059794,
            -51.1733438);
    private static final LatLng BROOKLYN_BRIDGE = new LatLng(-30.0599683, -51.1714104);
    private static final LatLng WALL_STREET = new LatLng(-30.061237, -51.172944);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        settings = getSharedPreferences(Constants.getMyPreferenceFile(), 0);
        markerButton = (Button) findViewById(R.id.markerButton);
        setUpMapIfNeeded();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLoationRequest();
        onMapReady(mMap);

        String url = getMapsApiDirectionsUrl(LOWER_MANHATTAN, BROOKLYN_BRIDGE);
        ReadTask downloadTask = new ReadTask();
        downloadTask.execute(url);

        markerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                LatLng latLng = new LatLng(-30.059794, -51.1733438);
                MarkerOptions newMarker = new MarkerOptions()
                        .position(latLng)
                        .draggable(true)
                        .title("Lat: " + latLng.latitude + " \nLong:" + latLng.longitude);
                Log.i(Constants.getAppName(), newMarker.getPosition().toString());
                mMap.addMarker(newMarker);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                Log.i("MAPS INFO: My location", mMap.getMyLocation().toString());
            }
        });
    }

    public void onMapReady(GoogleMap map) {
        /*
       * Load all point and setup map
       * */
        LatLng l = new LatLng(-30.059794, -51.1733438);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(l)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    protected void createLoationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
           // mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {
        mMap.setMyLocationEnabled(true);

    }


    private void handleNewLocation(Location location) {
        Log.d(Constants.getAppName(), location.toString());
/*
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        current = new MarkerOptions()
                .position(latLng)
                .title("Lat: " + latLng.latitude + " Long: " + latLng.longitude)
                .flat(true)
                .draggable(false);

        Log.i(TAG, current.getPosition().toString());

        mMap.addMarker(current);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

*/
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                marker.setTitle("Lat: " + marker.getPosition().latitude + " Long: " + marker.getPosition().longitude);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String lat = String.valueOf(marker.getPosition().latitude);
                String longi = String.valueOf(marker.getPosition().longitude);
                Intent i = new Intent(MapsActivityBackup.this, CriticalPointActivity.class);
                settings.edit().putString(Constants.getMarkerLatitude(), lat).commit();
                settings.edit().putString(Constants.getMarkerLongitude(), longi).commit();
                Log.i(Constants.getAppName(), "MAPS" + marker.getPosition().toString());
                startActivity(i);
                return true;
            }
        });

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(Constants.getAppName(), "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(Constants.getAppName(), "Location services connected");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(Constants.getAppName(), "Location services suspended. Please reconnect");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }


    private String getMapsApiDirectionsUrl(LatLng p1, LatLng p2) {
        String waypoints = "waypoints=optimize:true|"
                + p1.latitude + "," + p1.longitude
                + "|" + "|" + p2.latitude + ","
                + p2.longitude;

        String sensor = "sensor=false";
        String origin = "origin=" + p1.latitude + "," + p1.longitude;
        String destination = "destination=" + p2.latitude + "," + p2.longitude;
        String params = origin + "&" + destination + "&%20" + waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;

        return url;
    }


    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(4);
                polyLineOptions.color(Color.BLUE);
            }

            mMap.addPolyline(polyLineOptions);
        }
    }
}
