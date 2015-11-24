package com.pucrs.andepucrs.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.support.annotation.MainThread;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.directions.route.Route;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.pucrs.andepucrs.AndePUCRSApplication;
import com.pucrs.andepucrs.R;
import com.pucrs.andepucrs.api.AndePUCRSAPI;
import com.pucrs.andepucrs.api.Constants;
import com.pucrs.andepucrs.heuristic.AStarHeuristic;
import com.pucrs.andepucrs.heuristic.DiagonalHeuristic;
import com.pucrs.andepucrs.model.Estabelecimentos;
import com.pucrs.andepucrs.model.Favorite;
import com.pucrs.andepucrs.model.Map;
import com.pucrs.andepucrs.model.Ponto;
import com.pucrs.andepucrs.model.Preferencias;
import com.pucrs.andepucrs.pathFinder.AStar;
import com.pucrs.andepucrs.pathFinder.AreaMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, RoutingListener, SensorEventListener {
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private SharedPreferences settings;
    private MarkerOptions current;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Button markerButton;
    private Button favoriteButton;
    private Button commentButton;
    //private Button turnByTurnButton;
    private Button recalculateButton;
    private Location myfirstLocation;
    private Location myCurrentLocation;
    private LatLng search;
    private Estabelecimentos searchPoint;
    private boolean hadSearch;
    private boolean firstTime;
    private int goalX;
    private int goalY;
    private ArrayList<Map> mapToPrint;
    private ProgressBar mapProgressBar;
    private boolean doTrace;
    private boolean fromFavorite;
    private ArrayList<Ponto> allPoints;
    private int startPointLocaionX;
    private int startPointLocaionY;
    private Polyline polylineFinal;
    private int mapToPrintCompassCount;
    private boolean useGoogleDirections;
    private Estabelecimentos searchGoal;
    private PolylineOptions gDirectionsPolylineOptions;
    private Route gRoute;
    private TextToSpeech tts;
    private boolean printed;
    private AndePUCRSApplication app;


    /**
     * Sensor
     */

    private ImageView mPointer;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    private boolean traceDone;
    public static double measure(double lat1, double lon1, double lat2, double lon2) {
        double R = 6378.137;
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLon = (lon2 - lon1) * Math.PI / 180;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1 * Math.PI / 180)
                * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d * 1000;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        settings = getSharedPreferences(Constants.getMyPreferenceFile(), 0);
        markerButton = (Button) findViewById(R.id.markerButton);
        favoriteButton = (Button) findViewById(R.id.favoriteButton);
        commentButton = (Button) findViewById(R.id.commentButton);
        //turnByTurnButton = (Button) findViewById(R.id.turnbyturnButton);
        mapProgressBar = (ProgressBar) findViewById(R.id.mapProgressBar);
        recalculateButton = (Button) findViewById(R.id.recalculateButton);
        mapProgressBar.setVisibility(View.INVISIBLE);
        traceDone = false;
        printed = false;
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPointer = (ImageView) findViewById(R.id.pointer);

        gDirectionsPolylineOptions = new PolylineOptions();
        gRoute = new Route();

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.getDefault());
                }
            }
        });


        mapToPrintCompassCount = 0;
        firstTime = true;
        goalX = goalY = 0;
        mapToPrint = new ArrayList<>();
        doTrace = false;
        useGoogleDirections = false;
        /**
         * Read Seach points
         * */

        Intent intent = getIntent();
        boolean fromMenu = intent.getBooleanExtra("FromMenu", false);
        fromFavorite = intent.getBooleanExtra("FromFavorite", false);
        Gson gson = new Gson();
        String offlineData = settings.getString(Constants.getSerachPoint(), "");
        searchPoint = gson.fromJson(offlineData, Estabelecimentos.class);
        favoriteButton.setEnabled(false);
        commentButton.setEnabled(true);
        // turnByTurnButton.setEnabled(false);
        recalculateButton.setEnabled(false);
        if (fromMenu) {
            //if no search, send to library
            hadSearch = false;

            search = new LatLng(-30.058710, -51.173776);
        } else {
            if (searchPoint != null) {
                search = new LatLng(searchPoint.getLatitude(), searchPoint.getLongitude());
                hadSearch = true;
                doTrace = true;
            } else {
                hadSearch = false;
                search = new LatLng(-30.058710, -51.173776);
            }
        }


        setUpMapIfNeeded();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLoationRequest();
        onMapReady(mMap);

        markerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  mapToPrintCompassCount++;
             //   Log.d("DELTA", "" + mapToPrintCompassCount);
              //  Log.d("DELTA", ""+ mapToPrint.get(mapToPrintCompassCount).getLatitude()+""+ mapToPrint.get(mapToPrintCompassCount).getLongitude());
                LatLng latLng = mMap.getCameraPosition().target;
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
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
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
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setMessage("Você tem certeza que quer salvar essa rota como favorita ?");
                alertDialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        /**
                         * Read preferences
                         * */
                        Gson gson = new Gson();
                        String offlineData = settings.getString(Constants.getUserDataPreference(), "");
                        Preferencias[] p = gson.fromJson(offlineData, Preferencias[].class);
                        ArrayList<Preferencias> listPref;
                        if (p == null) {
                            listPref = new ArrayList<Preferencias>();
                        } else {
                            listPref = new ArrayList<>(Arrays.asList(p));
                        }
                        //Create new Favorite route
                        Favorite favoriteRoute = new Favorite(listPref, new LatLng(myfirstLocation.getLatitude(), myfirstLocation.getLongitude()), searchPoint);
                        /**
                         * Read previous favorite routes
                         * */
                        offlineData = settings.getString(Constants.getFavorite(), "");
                        Favorite[] f = gson.fromJson(offlineData, Favorite[].class);
                        ArrayList<Favorite> savedFavorite;
                        if (f == null) {
                            //First favorite route
                            savedFavorite = new ArrayList<Favorite>();
                            savedFavorite.add(favoriteRoute);
                            offlineData = gson.toJson(savedFavorite);
                            settings.edit().putString(Constants.getFavorite(), offlineData).commit();
                            Toast.makeText(MapsActivity.this, "Rota favorita cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                        } else {
                            boolean crash = false;
                            savedFavorite = new ArrayList<>(Arrays.asList(f));
                            for (Favorite fa : savedFavorite) {
                                if (fa.getFinish().getLatitude() == favoriteRoute.getFinish().getLatitude() ||
                                        fa.getFinish().getLongitude() == favoriteRoute.getFinish().getLongitude() ||
                                        fa.getStart().latitude == favoriteRoute.getStart().latitude ||
                                        fa.getStart().longitude == favoriteRoute.getStart().longitude) {
                                    crash = true;
                                }
                            }
                            if (!crash) {
                                savedFavorite.add(favoriteRoute);
                                offlineData = gson.toJson(savedFavorite);
                                settings.edit().putString(Constants.getFavorite(), offlineData).commit();
                                Toast.makeText(MapsActivity.this, "Rota favorita cadastrada com sucesso", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MapsActivity.this, "Rota já foi cadastrada como favorita", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                alertDialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MapsActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hadSearch) {
                    LatLng current = new LatLng(myfirstLocation.getLatitude(), myfirstLocation.getLongitude());
                    Gson gson = new Gson();
                    String offlineData = gson.toJson(current);
                    settings.edit().putString(Constants.getMyCurrentLocation(), offlineData).commit();
                    offlineData = gson.toJson(searchPoint);
                    settings.edit().putString(Constants.getSerachPoint(), offlineData).commit();
                    Intent i = new Intent(MapsActivity.this, CommentActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(MapsActivity.this, "Por favor, realize uma pesquisa para enviar um comentário", Toast.LENGTH_SHORT).show();
                }

            }
        });

       /* turnByTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, TurnByTurnActivity.class);
                startActivity(i);
            }
        });*/

        recalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setMessage("Você tem certeza que quer recalcular a rota ?");
                alertDialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MapsActivity.this, "Re-calculando", Toast.LENGTH_SHORT).show();
                        if (polylineFinal != null) {
                            polylineFinal.remove();
                        }
                        fromFavorite = false;
                        mapProgressBar.setVisibility(View.VISIBLE);
                        runAStar();
                    }
                });
                alertDialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MapsActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                printed = false;
            }
        });

        offlineData = settings.getString(Constants.getSerachPoint(), "");
        searchGoal = gson.fromJson(offlineData, Estabelecimentos.class);

        /**
         * Calculate the GOOGLE ROUTE
         * */
        if (hadSearch) {
            Log.d(Constants.getAppName(), "Starting Google Directions API");
            LatLng startGoogle = new LatLng(-30.061802, -51.174701);
            LatLng goalGoogle = new LatLng(searchGoal.getLatitude(), searchGoal.getLongitude());
            Routing routing = new Routing.Builder()
                    .travelMode(Routing.TravelMode.WALKING)
                    .withListener(MapsActivity.this)
                    .waypoints(startGoogle, goalGoogle)
                    .build();
            routing.execute();
        } else {
            printObstacle();
        }

    }

    public void printObstacle() {
        mapProgressBar.setVisibility(View.VISIBLE);
        if (!hadSearch) {
            app = (AndePUCRSApplication) getApplication();
            AndePUCRSAPI webService = app.getService();
            webService.findAllPoints(new Callback<ArrayList<Ponto>>() {
                @Override
                public void success(ArrayList<Ponto> pontos, Response response) {
                    allPoints = pontos;
                    for (Ponto obstacleToPrint : allPoints) {
                       // Log.d(Constants.getAppName(), "Obstacle Marker " + obstacleToPrint.getNroIntPref().getNroIntPref());
                        Marker obtacleMarker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(obstacleToPrint.getLatitude(), obstacleToPrint.getLongitude()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.obstacle_icon))
                                .title("Obstaculo, " + obstacleToPrint.getNroIntPref().getNome()));
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(Constants.getAppName() + "Allpoints", error.getMessage());
                }
            });
        }

        mapProgressBar.setVisibility(View.INVISIBLE);
    }


    @MainThread
    public void traceRoute() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mapToPrint == null) {
                    mapProgressBar.setVisibility(View.VISIBLE);
                   // Toast.makeText(MapsActivity.this, "Usando o google direction API", Toast.LENGTH_SHORT).show();
                    useGoogleDirections = true;
                    onRoutingSuccess(gDirectionsPolylineOptions, gRoute);
                } else {
                    Log.d(Constants.getAppName(), "Start Printing polylines on Google Map");
                    useGoogleDirections = false;
                    ArrayList<LatLng> poly = new ArrayList<>();
                    boolean add = false;
                    //Diminiu a quantidade de prints para 5 metros
                    for (Map point : mapToPrint) {
                        add = true;
                        if (poly.isEmpty()) {
                            poly.add(new LatLng(point.getLatitude(), point.getLongitude()));
                        } else {
                            for (LatLng pre : poly) {
                                double ponto = measure(pre.latitude, pre.longitude, point.getLatitude(), point.getLongitude());
                                if (ponto <= 2.5) {
                                    add = false;
                                }
                            }
                            if (add) {
                                poly.add(new LatLng(point.getLatitude(), point.getLongitude()));
                            }
                        }
                    }
                    //Log.d(Constants.getAppName(), "Polilyne Total Size" + poly.size());
                    PolylineOptions polyoptions = new PolylineOptions();
                    polyoptions.color(Color.BLUE);
                    polyoptions.width(10);
                    polyoptions.addAll(poly);
                    polylineFinal = mMap.addPolyline(polyoptions);

                    Marker goalMarker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(poly.get(poly.size() - 1).latitude, poly.get(poly.size() - 1).longitude))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.finish_line_flag))
                            .title("Destino"));
                    mapProgressBar.setVisibility(View.INVISIBLE);
                }
                for (Ponto obstacleToPrint : allPoints) {
                    if (obstacleToPrint.getNroIntPref() != null && obstacleToPrint.getNroIntPref().isSelected()) {
                        //      Log.d(Constants.getAppName(), "Obstacle Marker " + obstacleToPrint.getNroIntPref().getNroIntPref());
                        Marker obtacleMarker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(obstacleToPrint.getLatitude(), obstacleToPrint.getLongitude()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.obstacle_icon))
                                .title("Obstaculo, " + obstacleToPrint.getNroIntPref().getNome()));
                    }
                }
                printed = true;
                traceDone = true;
                favoriteButton.setEnabled(true);
                commentButton.setEnabled(true);
                //turnByTurnButton.setEnabled(true);
                recalculateButton.setEnabled(true);
            }
        });


    }


    private void runAStar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(Constants.getAppName(), "Start running A-Star algorithm");
                /**
                 * Carrega destino e mapa e o mapa de obstaculos
                 * */
                mapToPrint = new ArrayList<Map>();
                Gson gson = new Gson();
                double lat;
                double lng;
                double a, b, pa, hx, hy, cTop, cBot;
                int mapWith = 810;
                int mapHeight = 712;
                int startX;
                int startY;
                mapToPrintCompassCount = 0;
                /**
                 *Carrega mapa
                 * */
                String offlineData = settings.getString(Constants.getMap(), "");
                Map[] aux = gson.fromJson(offlineData, Map[].class);
                ArrayList<Map> mapAll = new ArrayList<>(Arrays.asList(aux));
                Log.d(Constants.getAppName(), "Original Map size " + mapAll.size());

                offlineData = settings.getString(Constants.getObstacleMap(), "");
                int[][] obstacleMap = gson.fromJson(offlineData, int[][].class);

                /*
                String offlineData = settings.getString(Constants.getDestX(), "");
                goalX = (int) gson.fromJson(offlineData, int.class);
                offlineData = settings.getString(Constants.getDestY(), "");
                goalY = (int) gson.fromJson(offlineData, int.class);
                */


                /**
                 * Ajusta ponto de destino
                 * */
                lat = searchGoal.getLatitude();
                lng = searchGoal.getLongitude();
                cTop = 810;
                cBot = 712;
                a = measure(-30.055759900000005, -51.1774278, lat, lng);
                b = measure(-30.055704100000003, -51.1690164, lat, lng);
                // calculate x
                pa = (a + b + cTop) / 2;
                hy = (2 / cTop) * (Math.sqrt((pa * (pa - a) * (pa - b) * (pa - cTop))));
                // Calculate y
                b = measure(-30.0621486, -51.177588699999994, lat, lng);
                pa = (a + b + cBot) / 2;
                hx = (2 / cBot) * (Math.sqrt((pa * (pa - a) * (pa - b) * (pa - cBot))));
                int around = 20;
                for (int i = (int) hy - around; i < hy + around; i++) {
                    for (int j = (int) hx - around; j < hx + around; j++) {
                        obstacleMap[i][j] = 0;
                    }
                }

                goalX = (int) hy;
                goalY = (int) hx;

                /**
                 * Adiciona o usuario no mapa baseado na sua location
                 * */
                boolean hit = false;
                around = 1;
/*
                while (!hit) {
                    for (int i = startPointLocaionX - around; i < startPointLocaionX + around && !hit; i++) {
                        for (int j = startPointLocaionY - around; j < startPointLocaionY + around && !hit; j++) {
                            if (obstacleMap[i][j] == 0) {
                                hit = true;
                                startPointLocaionX = i;
                                startPointLocaionY = j;
                                Log.d(Constants.getAppName(), "Start Position on obstacle map" + i + ", " + j);
                            }
                        }
                    }
                    around++;
                }*/

                /**
                 * Carrega perfil, se pelos favoritos ou search normal
                 * */
                if (fromFavorite) {
                    offlineData = settings.getString(Constants.getUserDataPreferenceReDO(), "");
                } else {
                    offlineData = settings.getString(Constants.getUserDataPreference(), "");
                }

                //garante que pref não é null
                Preferencias[] pref = gson.fromJson(offlineData, Preferencias[].class);
                final ArrayList<Preferencias> allpreferences;
                if (pref == null)
                    allpreferences = new ArrayList();
                else
                    allpreferences = new ArrayList<>(Arrays.asList(pref));

                /**
                 * Mapea os pontos baseado na pref
                 * */
                offlineData = settings.getString(Constants.getAllPoints(), "");
                Ponto[] ponto = gson.fromJson(offlineData, Ponto[].class);
                allPoints = new ArrayList<>(Arrays.asList(ponto));
                for (Ponto p : allPoints) {
                    for (Preferencias preferencias : allpreferences) {
                        if (p.getNroIntPref() != null) {
                            if (p.getNroIntPref().getNroIntPref() == preferencias.getNroIntPref()) {
                                p.getNroIntPref().setSelected(preferencias.isSelected());
                            }
                        }
                    }
                }

                /**
                 * ajusta mapa, adicionando mais pontos ao redor das rotas
                 */

                //need to improve this;
                for (int x = 0; x < 810; x++) {
                    for (int y = 0; y < 712; y++) {
                        if (obstacleMap[x][y] == 0) {
                            for (int i = x - 1; i < x + 1; i++) {
                                for (int j = y - 1; j < y + 1; j++) {
                                    obstacleMap[i][j] = 0;
                                }
                            }
                        }
                    }
                }

                /**
                 * carrega todos os pontos criticos, baseado na pref;
                 */
                for (Ponto p : allPoints) {
                    if (p.getNroIntPref() != null && p.getNroIntPref().isSelected()) {
                        Log.d(Constants.getAppName(), "Obstaculo " + p.getNroIntPref().getNome() + " - " + p.getLatitude() + ", " + p.getLongitude() + " - " + p.getNroIntPref().getValor());
                        lat = p.getLatitude();
                        lng = p.getLongitude();
                        cTop = 810;
                        cBot = 712;
                        a = measure(-30.055759900000005, -51.1774278, lat, lng);
                        b = measure(-30.055704100000003, -51.1690164, lat, lng);
                        // calculate y
                        pa = (a + b + cTop) / 2;
                        hy = (2 / cTop) * (Math.sqrt((pa * (pa - a) * (pa - b) * (pa - cTop))));
                        // Calculate x
                        b = measure(-30.0621486, -51.177588699999994, lat, lng);
                        pa = (a + b + cBot) / 2;
                        hx = (2 / cBot) * (Math.sqrt((pa * (pa - a) * (pa - b) * (pa - cBot))));
                        around = 20;
                        for (int i = (int) hy - around; i < hy + around; i++) {
                            for (int j = (int) hx - around; j < hx + around; j++) {
                                obstacleMap[i][j] = 1;
                            }
                        }
                    }
                }
                Log.d(Constants.getAppName(), "Goal position: " + goalX + ", " + goalY);


                startX = 665;
                startY = 223;

                startX = 652;
                startY = 275;
                /**
                 * From Estacionamento to RU
                 * */
/*                startX = 652;
                startY = 275;
                goalX = 70;
                goalY = 522;
*/
                /**
                 * From RU to Estacionamento
                 * */
                /*
                goalX = 652;
                goalY = 275;
                startX = 70;
                startY = 522;
*/

                /*
                goalX = 70;
                goalY = 522;
                startX = 598;
                startY = 392;
                goalX = 421;
                goalY = 180;
*/
                AreaMap mapResult = new AreaMap(mapWith, mapHeight, obstacleMap);
                AStarHeuristic heuristic = new DiagonalHeuristic();
                AStar aStar = new AStar(mapResult, heuristic);
                ArrayList<Point> shortestPath = aStar.calcShortestPath(startX, startY, goalX, goalY);

                if (shortestPath != null) {
                    Log.d(Constants.getAppName(), "ShortestPath Size: " + shortestPath.size());
                    for (Point p : shortestPath) {
                        for (Map m : mapAll) {
                            if (m.getX() == p.y && m.getY() == p.x) {
                                mapToPrint.add(m);
                            }
                        }
                    }
                } else {
                    mapToPrint = null;
                    Log.d(Constants.getAppName(), "Could not do the search");
                }
                traceRoute();
            }

        }).start();
    }

    public void onMapReady(GoogleMap map) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(search)      // Sets the center of the map to Mountain View
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
                .setFastestInterval(1000); // 1 second, in milliseconds
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
                if (!marker.getTitle().contains("Obstaculo") && !marker.getTitle().contains("Destino")) {
                    String lat = String.valueOf(marker.getPosition().latitude);
                    String longi = String.valueOf(marker.getPosition().longitude);
                    Intent i = new Intent(MapsActivity.this, CriticalPointActivity.class);
                    settings.edit().putString(Constants.getMarkerLatitude(), lat).commit();
                    settings.edit().putString(Constants.getMarkerLongitude(), longi).commit();
                    Log.i(Constants.getAppName(), "MAPS" + marker.getPosition().toString());
                    startActivity(i);
                } else {
                    String toSpeak = marker.getTitle().toString();
                    tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }
                return true;
            }
        });

        if (location == null) {
            //biblioteca
            location.setLatitude(-30.059794);
            location.setLongitude(-51.1733438);
        } else {
            myCurrentLocation = location;
        }

        if (firstTime) {
            myfirstLocation = location;
            myCurrentLocation = location;
            /**
             * ADD PEOPLE TO MAP
             * */
            double lat;
            double lng;
            double a, b, pa, hx, hy, cTop, cBot;
            lat = myfirstLocation.getLatitude();
            lng = myfirstLocation.getLongitude();

            cTop = 810;
            cBot = 712;
            a = measure(-30.055759900000005, -51.1774278, lat, lng);
            b = measure(-30.055704100000003, -51.1690164, lat, lng);
            // calculate x
            pa = (a + b + cTop) / 2;
            hy = (2 / cTop) * (Math.sqrt((pa * (pa - a) * (pa - b) * (pa - cTop))));
            // Calculate y
            b = measure(-30.0621486, -51.177588699999994, lat, lng);
            pa = (a + b + cBot) / 2;
            hx = (2 / cBot) * (Math.sqrt((pa * (pa - a) * (pa - b) * (pa - cBot))));

            startPointLocaionX = (int) hy;
            startPointLocaionY = (int) hx;

            Log.d(Constants.getAppName(), "My Location on map " + startPointLocaionX + ", " + startPointLocaionY);
            firstTime = false;
            if (doTrace) {
                doTrace = false;
                mapProgressBar.setVisibility(View.VISIBLE);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage(
                        "Por favor, note que a rota pode estar desatualizada, devido a mudanças no ambiente.\n" +
                        "Se estiver, por favor, envie o novo obstáculo para o servidor.\n" +
                        "Assim, contribuindo com a aplicação.");
                alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        runAStar();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        }
        if (!hadSearch) {
            mapProgressBar.setVisibility(View.INVISIBLE);
        }
        if (printed) {
            Log.d(Constants.getAppName(), "Chamou camera");
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude()))      // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
                    .bearing(0)                // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
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
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onRoutingFailure() {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(PolylineOptions polylineOptions, Route route) {

        if (useGoogleDirections) {
            Log.d(Constants.getAppName(), "Called, onROutingSuccess");
            mapProgressBar.setVisibility(View.INVISIBLE);
            PolylineOptions polyoptions = new PolylineOptions();
            polyoptions.color(Color.BLUE);
            polyoptions.width(10);
            polyoptions.addAll(polylineOptions.getPoints());
            mMap.addPolyline(polyoptions);
            mapToPrint = new ArrayList<>();
            ArrayList<LatLng> pontos = (ArrayList) polylineOptions.getPoints();
            for (LatLng l : pontos) {
                mapToPrint.add(new Map(l.latitude, l.longitude));
            }
            traceDone = true;
        } else {
            gDirectionsPolylineOptions = polylineOptions;
            gRoute = route;
        }
    }


    @Override
    public void onRoutingCancelled() {
        mMap.clear();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            if (mapToPrint != null && mapToPrint.size() > 0) {
                if (myCurrentLocation != null && mapToPrintCompassCount < mapToPrint.size()) {
                    double delta = measure(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude(), mapToPrint.get(mapToPrintCompassCount).getLatitude(), mapToPrint.get(mapToPrintCompassCount).getLongitude());
                    if (delta <= 5) {
                        mapToPrintCompassCount +=5;
                        Log.d("DELTA","Delta: "+delta+"Count:" + mapToPrintCompassCount);
                    }
                    float azimuth = mOrientation[0];
                    Location currentLoc = myCurrentLocation;
                    Location target = new Location("");
                    target.setLatitude(mapToPrint.get(mapToPrintCompassCount).getLatitude());
                    target.setLongitude(mapToPrint.get(mapToPrintCompassCount).getLongitude());
                    azimuth = (float) Math.toDegrees(azimuth);
                    GeomagneticField geoField = new GeomagneticField(
                            (float) currentLoc.getLatitude(),
                            (float) currentLoc.getLongitude(),
                            (float) currentLoc.getAltitude(),
                            System.currentTimeMillis());
                    azimuth += geoField.getDeclination(); // converts magnetic north into true north

                    float bearing = currentLoc.bearingTo(target); // (it's already in degrees)
                    float direction = azimuth - bearing;

                    RotateAnimation ra = new RotateAnimation(
                            mCurrentDegree,
                            -direction,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF,
                            0.5f);

                    ra.setDuration(250);
                    ra.setFillAfter(true);
                    mPointer.startAnimation(ra);
                    mCurrentDegree = -direction;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if(traceDone){
            double delta = measure(myCurrentLocation.getLatitude(), myCurrentLocation.getLongitude(), mapToPrint.get(mapToPrintCompassCount).getLatitude(), mapToPrint.get(mapToPrintCompassCount).getLongitude());
            if (delta <= 5){
                mapToPrintCompassCount++;
                Log.d("DELTA","Accurracy Delta: "+delta+"Count:" + mapToPrintCompassCount);
            }
        }
    }
}