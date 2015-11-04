package com.pucrs.andepucrs.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.pucrs.andepucrs.R;
import com.pucrs.andepucrs.api.Constants;
import com.pucrs.andepucrs.model.Estabelecimentos;
import com.pucrs.andepucrs.model.Favorite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private SharedPreferences settings;
    private ListView listView;
    private ArrayList<Favorite> savedFavorite;
    private ArrayList<Estabelecimentos> allEstablishments;
    private int selectedPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        settings = getSharedPreferences(Constants.getMyPreferenceFile(), 0);
        listView = (ListView) findViewById(R.id.favoriteListView);
        final Gson gson = new Gson();
        String offlineData = settings.getString(Constants.getFavorite(), "");
        Favorite[] f = gson.fromJson(offlineData, Favorite[].class);
        if (f != null) {
            savedFavorite = new ArrayList<>(Arrays.asList(f));
            offlineData = settings.getString(Constants.getEstablishments(), "");
            Estabelecimentos[] e = gson.fromJson(offlineData, Estabelecimentos[].class);
            allEstablishments = new ArrayList<>(Arrays.asList(e));
            createList(savedFavorite);
            AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String resultListView = parent.getItemAtPosition(position).toString();
                    String offline = gson.toJson(savedFavorite.get(position));
                    settings.edit().putString(Constants.getFavoriteDetails(), offline).commit();
                    Intent i = new Intent(FavoriteActivity.this, FavoriteDetailsActivity.class);
                    startActivity(i);
                }
            };
            final AdapterView.OnItemClickListener listener = onItemClickListener;
            listView.setOnItemClickListener(listener);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedPosition = position;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                    alertDialogBuilder.setMessage("Você tem certeza que quer deletar essa rota ?");
                    alertDialogBuilder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            savedFavorite.remove(selectedPosition);
                            createList(savedFavorite);
                            /**
                             * Save data offline
                             * */
                            Gson gson = new Gson();
                            String offlineData = gson.toJson(savedFavorite);
                            settings.edit().putString(Constants.getFavorite(), offlineData).commit();
                        }
                    });

                    alertDialogBuilder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(FavoriteActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return true;
                }
            });
        } else {
            Toast.makeText(FavoriteActivity.this, "Você não possue rotas favoritas salvas", Toast.LENGTH_SHORT).show();
        }
    }


    private void createList(ArrayList<Favorite> pref) {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        for (Favorite p : pref) {
            categories.add("De: " + p.getStart().toString() + "até: " + p.getFinish().getNome());
        }
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        // attaching data adapter to spinner
        listView.setAdapter(dataAdapter);
    }

    private String searchEstabishmentBaseOnLatLng(ArrayList<Estabelecimentos> establishments, LatLng searchQuery) {
        Estabelecimentos resultEs = null;
        for (Estabelecimentos l : establishments) {
            if (l.getLatitude() == searchQuery.latitude && l.getLongitude() == searchQuery.longitude) {
                resultEs = l;
            }
        }
        return resultEs.getNome();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_maps) {
            i = new Intent(FavoriteActivity.this, MapsActivity.class);
            i.putExtra("FromMenu", true);
            startActivity(i);
        }
        if (id == R.id.action_profile) {
            i = new Intent(FavoriteActivity.this, ProfileSetupActivity.class);

            startActivity(i);
        }
        if (id == R.id.action_search) {
            i = new Intent(FavoriteActivity.this, SearchActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_favorite) {
            i = new Intent(FavoriteActivity.this, FavoriteActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_user) {
            i = new Intent(FavoriteActivity.this, UserConfiguration.class);
            startActivity(i);
        }
        if (id == R.id.action_logout) {
            if(settings.getBoolean(Constants.getSession(),false)){
                settings.edit().putBoolean(Constants.getSession(), false).commit();
                Toast.makeText(FavoriteActivity.this, "Usuário desconectou", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(FavoriteActivity.this, "Nenhum usuário conectado para fazer logoff", Toast.LENGTH_SHORT).show();
            }
            i = new Intent(FavoriteActivity.this, SearchActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
