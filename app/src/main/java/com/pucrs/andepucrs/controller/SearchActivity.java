package com.pucrs.andepucrs.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pucrs.andepucrs.AndePUCRSApplication;
import com.pucrs.andepucrs.R;
import com.pucrs.andepucrs.api.AndePUCRSAPI;
import com.pucrs.andepucrs.api.Constants;
import com.pucrs.andepucrs.model.Establishment;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchActivity extends AppCompatActivity {

    private AndePUCRSApplication app;
    private ArrayList<Establishment> establishmentList;
    private EditText searchEditText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchEditText == null || searchEditText.getText().equals("") || searchEditText.getText() == null) {
                    Toast.makeText(SearchActivity.this, "Digite algo na pesquisa", Toast.LENGTH_SHORT).show();
                } else {

                    app = (AndePUCRSApplication) getApplication();

                    AndePUCRSAPI webService = app.getService();
                    webService.findAllLocations(new Callback<ArrayList<Establishment>>() {
                        @Override
                        public void success(ArrayList<Establishment> establishments, Response response) {
                            establishmentList = establishments;
                            ArrayList<String> result = searchEstabishment(searchEditText.getText().toString());
                            if(result.size() == 1){

                            }
                            Toast.makeText(SearchActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SearchActivity.this, MapsActivity.class);
                            startActivity(i);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(Constants.getAppName(), error.toString());
                        }
                    });
                }
            }
        });


    }

    private ArrayList<String> searchEstabishment(String searchQuery) {
        ArrayList<String> result = new ArrayList<>();
        for (Establishment l : establishmentList) {
            if (l.toString().contains(searchQuery)) {
                 result.add(l.getNome());
            }
        }
        return result;
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

}
