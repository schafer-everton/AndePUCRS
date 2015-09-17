package com.pucrs.andepucrs.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pucrs.andepucrs.AndePUCRSApplication;
import com.pucrs.andepucrs.R;
import com.pucrs.andepucrs.api.AndePUCRSAPI;
import com.pucrs.andepucrs.api.Constants;
import com.pucrs.andepucrs.model.Estabelecimentos;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private AndePUCRSApplication app;
    private SharedPreferences settings;
    private EditText searchEditText;
    private Button searchButton;
    private Spinner spinner;
    private String resultSpinner;
    private ProgressBar searchProgressBar;
    private TextView searchTextview;
    private ArrayList<Estabelecimentos> result;

    private boolean gambi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        settings = getSharedPreferences(Constants.getMyPreferenceFile(), 0);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchButton = (Button) findViewById(R.id.searchButton);
        spinner = (Spinner) findViewById(R.id.searchSpinner);
        searchProgressBar = (ProgressBar) findViewById(R.id.searchProgressBar);
        searchProgressBar.setVisibility(View.INVISIBLE);
        searchTextview = (TextView) findViewById(R.id.searchTextview);
        spinner.setEnabled(false);
        searchTextview.setVisibility(View.INVISIBLE);
        spinner.setOnItemSelectedListener(this);
        gambi = false;


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProgressBar.setVisibility(View.VISIBLE);
                gambi = false;
                if (searchEditText == null || searchEditText.getText().equals("") || searchEditText.getText() == null) {
                    Toast.makeText(SearchActivity.this, "Digite algo na pesquisa", Toast.LENGTH_SHORT).show();

                } else {
                    app = (AndePUCRSApplication) getApplication();
                    AndePUCRSAPI webService = app.getService();
                    webService.findAllLocations(new Callback<ArrayList<Estabelecimentos>>() {
                        @Override
                        public void success(ArrayList<Estabelecimentos> establishments, Response response) {
                            result = searchEstabishment(establishments, searchEditText.getText().toString());
                            if (result.size() == 1 && (!result.isEmpty()) && !result.toString().equals("")) {
                                searchTextview.setVisibility(View.INVISIBLE);
                                Toast.makeText(SearchActivity.this, searchEstabishmentBaseOnName(establishments, result.get(0).getNome()).toString(), Toast.LENGTH_LONG).show();
                                Gson gson = new Gson();
                                String latitude = gson.toJson(result.get(0).getLatitude());
                                String longitude = gson.toJson(result.get(0).getLongitude());
                                settings.edit().putString(Constants.getSerachLatitude(),latitude ).commit();
                                settings.edit().putString(Constants.getSerachLongitude(), longitude).commit();

                                Intent i = new Intent(SearchActivity.this, MapsActivity.class);
                                startActivity(i);
                            } else {
                                spinner.setEnabled(true);
                                createSpinner(result);
                                searchTextview.setVisibility(View.VISIBLE);
                            }
                            searchProgressBar.setVisibility(View.INVISIBLE);
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
        });
    }

    private Estabelecimentos searchEstabishmentBaseOnName(ArrayList<Estabelecimentos> establishments, String searchQuery) {
        Estabelecimentos resultEs = null;
        for (Estabelecimentos l : establishments) {
            if (l.getNome().contains(searchQuery)) {
                resultEs = l;
            }
        }
        return resultEs;
    }

    private ArrayList<Estabelecimentos> searchEstabishment(ArrayList<Estabelecimentos> establishments, String searchQuery) {
        ArrayList<Estabelecimentos> result = new ArrayList<>();
        for (Estabelecimentos l : establishments) {
            if (l.getNome().contains(searchQuery)) {
                result.add(l);
            }
        }
        return result;
    }

    private void createSpinner(ArrayList<Estabelecimentos> pref) {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (Estabelecimentos p : pref) {
            categories.add(p.getNome());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SearchActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!gambi) {
            gambi = true;
            resultSpinner = parent.getItemAtPosition(position).toString();
        } else {
            resultSpinner = parent.getItemAtPosition(position).toString();
            Gson gson = new Gson();
            Estabelecimentos e = searchEstabishmentBaseOnName(result,resultSpinner);
            String latitude = gson.toJson(e.getLatitude());
            String longitude = gson.toJson(e.getLongitude());
            settings.edit().putString(Constants.getSerachLatitude(),latitude ).commit();
            settings.edit().putString(Constants.getSerachLongitude(),longitude ).commit();
            Toast.makeText(SearchActivity.this, resultSpinner, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
