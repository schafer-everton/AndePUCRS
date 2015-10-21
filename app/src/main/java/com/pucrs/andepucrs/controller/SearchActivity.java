package com.pucrs.andepucrs.controller;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pucrs.andepucrs.AndePUCRSApplication;
import com.pucrs.andepucrs.R;
import com.pucrs.andepucrs.api.AndePUCRSAPI;
import com.pucrs.andepucrs.api.Constants;
import com.pucrs.andepucrs.model.Estabelecimentos;
import com.pucrs.andepucrs.model.Map;
import com.pucrs.andepucrs.model.Ponto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchActivity extends AppCompatActivity {


    private final int REQ_CODE_SPEECH_INPUT = 100;
    private AndePUCRSApplication app;
    private SharedPreferences settings;
    private EditText searchEditText;
    private Button searchButton;
    private ListView listView;
    private String resultListView;
    private ProgressBar searchProgressBar;
    private TextView searchTextview;
    private ArrayList<Estabelecimentos> result;
    private ArrayList<Estabelecimentos> allEstabilishments;
    private ArrayList<Ponto> allPoints;
    private ImageButton btnSpeak;
    private ArrayList<Map> map;
    private ArrayList<Map> mapToPrint;
    private int obstacleMap[][];
    private Estabelecimentos search;

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
        setContentView(R.layout.activity_search);
        settings = getSharedPreferences(Constants.getMyPreferenceFile(), 0);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchButton = (Button) findViewById(R.id.searchButton);
        listView = (ListView) findViewById(R.id.searchListView);
        searchProgressBar = (ProgressBar) findViewById(R.id.searchProgressBar);
        searchProgressBar.setVisibility(View.INVISIBLE);
        btnSpeak = (ImageButton) findViewById(R.id.speakSearchButton);
        allPoints = new ArrayList<>();
        map = new ArrayList<>();
        mapToPrint = new ArrayList<>();
        obstacleMap = new int[Constants.getxMapSize()][Constants.getyMapSize()];
        search = null;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchProgressBar.setVisibility(View.VISIBLE);
                resultListView = parent.getItemAtPosition(position).toString();
                Gson gson = new Gson();
                Estabelecimentos e = searchEstabishmentBaseOnName(result, resultListView);
                search = e;

                String searchPoint = gson.toJson(e);
                settings.edit().putString(Constants.getSerachPoint(), searchPoint).commit();
                Toast.makeText(SearchActivity.this, resultListView, Toast.LENGTH_SHORT).show();
                loadMap();
            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });
    }

    public void createMatrix() {
        Log.d(Constants.getAppName(), "Creating Obstacle Map");
        int countPoint = 0;
        for (int i = 0; i < Constants.getxMapSize(); i++) {
            for (int j = 0; j < Constants.getyMapSize(); j++) {
                obstacleMap[i][j] = 1;
            }
        }
        //create map points
        for (Map m : map) {
            obstacleMap[m.getX()][m.getY()] = 0;
        }

        //ajusta mapa, adicionando mais pontos ao redor das rotas
        for (int x = 0; x < 810; x++) {
            for (int y = 0; y < 712; y++) {
                if (obstacleMap[x][y] == 0) {
                    for (int i = x - 1; i < x + 1; i++) {
                        for (int j = y - 1; j < y + 1; j++) {
                            obstacleMap[i][j]  = 0;
                            countPoint++;
                        }
                    }
                }
            }
        }

        Log.d(Constants.getAppName(),"Obstacle Map size (Improved) "+countPoint);

        app = (AndePUCRSApplication) getApplication();
        AndePUCRSAPI webService = app.getService();


        webService.findAllPoints(new Callback<ArrayList<Ponto>>() {
            @Override
            public void success(ArrayList<Ponto> pontos, Response response) {
                double lat;
                double lng;
                double a, b, pa, hx, hy, cTop, cBot;
                allPoints = pontos;
                //adiciona destino no obstacle map
                lat = search.getLatitude();
                lng = search.getLongitude();
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

                int around = 10;
                for (int i = (int) hy - around; i < hy + around; i++) {
                    for (int j = (int) hx - around; j < hx + around; j++) {
                        obstacleMap[i][j] = 0;
                    }
                }

                Gson gson =  new Gson();
                String store = gson.toJson(allPoints);
                settings.edit().putString(Constants.getAllPoints(), store).commit();

                String offlineData = gson.toJson(obstacleMap);
                settings.edit().putString(Constants.getObstacleMap(), offlineData).commit();

                offlineData = gson.toJson(map);
                settings.edit().putString(Constants.getMap(), offlineData).commit();

                offlineData = gson.toJson((int) hy);
                settings.edit().putString(Constants.getDestX(), offlineData).commit();

                offlineData = gson.toJson((int) hx);
                settings.edit().putString(Constants.getDestY(), offlineData).commit();
                startMap();
            }

            @Override
            public void failure(RetrofitError error) {
                searchProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SearchActivity.this, "Falha ao comunicar com o servidor, verifique a sua conexão", Toast.LENGTH_LONG).show();
            }
        });


    }

    public void doSearch() {
        searchProgressBar.setVisibility(View.VISIBLE);
        if (searchEditText == null || searchEditText.getText().equals("") || searchEditText.getText() == null) {
            Toast.makeText(SearchActivity.this, "Digite algo na pesquisa", Toast.LENGTH_SHORT).show();
        } else {
            app = (AndePUCRSApplication) getApplication();
            AndePUCRSAPI webService = app.getService();
            webService.findAllLocations(new Callback<ArrayList<Estabelecimentos>>() {
                @Override
                public void success(ArrayList<Estabelecimentos> establishments, Response response) {
                    allEstabilishments = establishments;
                    Collections.sort(establishments, new Comparator<Estabelecimentos>() {
                        @Override
                        public int compare(Estabelecimentos fruite1, Estabelecimentos fruite2) {
                            return fruite1.getNome().compareTo(fruite2.getNome());
                        }
                    });
                    result = searchEstabishment(establishments, searchEditText.getText().toString());
                    createList(result);
                    searchProgressBar.setVisibility(View.INVISIBLE);
                    /**
                     * Save data offline
                     * */
                    Gson gson = new Gson();
                    String offlineData = gson.toJson(allEstabilishments);
                    settings.edit().putString(Constants.getEstablishments(), offlineData).commit();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(SearchActivity.this, "Falha ao comunicar com o Servidor, por favor, verifique a sua conexão", Toast.LENGTH_SHORT).show();
                    Log.e(Constants.getAppName(), error.toString());
                    searchProgressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private void loadMap() {
        app = (AndePUCRSApplication) getApplication();
        final AndePUCRSAPI webService = app.getService();
        map = new ArrayList<>();
        webService.getMapFromTo(0, 1000, new Callback<ArrayList<Map>>() {
            @Override
            public void success(ArrayList<Map> maps, Response response) {
                map.addAll(maps);
                webService.getMapFromTo(1001, 2000, new Callback<ArrayList<Map>>() {
                    @Override
                    public void success(ArrayList<Map> maps, Response response) {
                        map.addAll(maps);
                        webService.getMapFromTo(2001, 3000, new Callback<ArrayList<Map>>() {
                            @Override
                            public void success(ArrayList<Map> maps, Response response) {
                                map.addAll(maps);
                                webService.getMapFromTo(3001, 4000, new Callback<ArrayList<Map>>() {
                                    @Override
                                    public void success(ArrayList<Map> maps, Response response) {
                                        map.addAll(maps);

                                        webService.getMapFromTo(4001, 5000, new Callback<ArrayList<Map>>() {
                                            @Override
                                            public void success(ArrayList<Map> maps, Response response) {
                                                map.addAll(maps);
                                                webService.getMapFromTo(5001, 6000, new Callback<ArrayList<Map>>() {
                                                    @Override
                                                    public void success(ArrayList<Map> maps, Response response) {
                                                        map.addAll(maps);

                                                        webService.getMapFromTo(6001, 7000, new Callback<ArrayList<Map>>() {
                                                            @Override
                                                            public void success(ArrayList<Map> maps, Response response) {
                                                                map.addAll(maps);
                                                                createMatrix();
                                                            }

                                                            @Override
                                                            public void failure(RetrofitError error) {
                                                                searchProgressBar.setVisibility(View.INVISIBLE);
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void failure(RetrofitError error) {
                                                        searchProgressBar.setVisibility(View.INVISIBLE);
                                                    }
                                                });
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {
                                                searchProgressBar.setVisibility(View.INVISIBLE);
                                            }
                                        });
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        searchProgressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                searchProgressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(Constants.getAppName(), error.getMessage());
            }
        });
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchEditText.setText(result.get(0));
                    doSearch();
                }
                break;
            }

        }
    }

    private void startMap() {
        searchProgressBar.setVisibility(View.INVISIBLE);
        Intent i = new Intent(SearchActivity.this, MapsActivity.class);
        i.putExtra("FromMenu", false);
        startActivity(i);
    }

    private Estabelecimentos searchEstabishmentBaseOnName(ArrayList<Estabelecimentos> establishments, String searchQuery) {
        Estabelecimentos resultEs = null;
        for (Estabelecimentos l : establishments) {
            if (l.getNome().equalsIgnoreCase(searchQuery)) {
                resultEs = l;
            }
        }
        return resultEs;
    }

    private ArrayList<Estabelecimentos> searchEstabishment(ArrayList<Estabelecimentos> establishments, String searchQuery) {
        ArrayList<Estabelecimentos> result = new ArrayList<>();
        for (Estabelecimentos l : establishments) {
            if (l.getNome().toLowerCase().contains(searchQuery.toLowerCase()) || l.getDescricao().toLowerCase().contains(searchQuery.toLowerCase())) {
                result.add(l);
            }
        }
        return result;
    }

    private void createList(ArrayList<Estabelecimentos> pref) {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        for (Estabelecimentos p : pref) {
            categories.add(p.getNome());
        }
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        // attaching data adapter to spinner
        listView.setAdapter(dataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent i;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_maps) {
            i = new Intent(SearchActivity.this, MapsActivity.class);
            i.putExtra("FromMenu", true);
            startActivity(i);
        }
        if (id == R.id.action_profile) {
            i = new Intent(SearchActivity.this, ProfileSetupActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_search) {
            i = new Intent(SearchActivity.this, SearchActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_favorite) {
            i = new Intent(SearchActivity.this, FavoriteActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SearchActivity.this, HomeActivity.class);
        startActivity(i);
    }

}



