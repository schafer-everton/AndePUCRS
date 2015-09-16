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

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.pucrs.andepucrs.AndePUCRSApplication;
import com.pucrs.andepucrs.R;
import com.pucrs.andepucrs.api.AndePUCRSAPI;
import com.pucrs.andepucrs.api.Constants;
import com.pucrs.andepucrs.model.Ponto;
import com.pucrs.andepucrs.model.PontoUsuario;
import com.pucrs.andepucrs.model.PontoUsuarioPK;
import com.pucrs.andepucrs.model.Preferencias;
import com.pucrs.andepucrs.model.Usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CriticalPointActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = Constants.getAppName();
    SharedPreferences settings;
    private Spinner preferencesSpinner;
    private TextView errorTextView;
    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private Button criticalPointButton;
    private AndePUCRSApplication app;
    private ProgressBar pbar;
    private String selectedItem;
    private EditText commentEditText;
    private PontoUsuario pontoUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critical_point);
        settings = getSharedPreferences(Constants.getMyPreferenceFile(), 0);
        pontoUsuario = new PontoUsuario();
        preferencesSpinner = (Spinner) findViewById(R.id.preferencesSpinner);
        errorTextView = (TextView) findViewById(R.id.errorTextView);
        latitudeTextView = (TextView) findViewById(R.id.latitudeTextView);
        longitudeTextView = (TextView) findViewById(R.id.longitudeTextView);
        criticalPointButton = (Button) findViewById(R.id.criticalPointButton);
        commentEditText = (EditText) findViewById(R.id.commentEditText);
        pbar = (ProgressBar) findViewById(R.id.progressBarCriticalPoint);
        pbar.setVisibility(View.VISIBLE);
        criticalPointButton.setEnabled(false);
        preferencesSpinner.setOnItemSelectedListener(this);

        final int userID = settings.getInt(Constants.getUserId(), 0);
        Boolean isLoggedIn = settings.getBoolean(Constants.getSession(), false);

        if (!isLoggedIn) {
            Intent i = new Intent(CriticalPointActivity.this, LoginActivity.class);
            i.putExtra(Constants.getRedirect(), "CriticalPointActivity");
            startActivity(i);
        } else {
            String pointLat = settings.getString(Constants.getMarkerLatitude(), "");
            String pointLong = settings.getString(Constants.getMarkerLongitude(), "");

            try {
                final LatLng latLng = new LatLng(Double.parseDouble(pointLat), Double.parseDouble(pointLong));
                latitudeTextView.setText("Latitude: " + latLng.latitude);
                longitudeTextView.setText("Longitude: " + latLng.longitude);
                errorTextView.setText("Usuario ID: " + userID +
                        "\nisLoggedIn: " + isLoggedIn);
                app = (AndePUCRSApplication) getApplication();
                final AndePUCRSAPI api = app.getService();
                api.findAllPreferences(new Callback<ArrayList<Preferencias>>() {
                    @Override
                    public void success(ArrayList<Preferencias> preferenciases, Response response) {
                        createSpinner(preferenciases);
                        criticalPointButton.setEnabled(true);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(Constants.getAppName(), error.toString());
                        Gson gson = new Gson();
                        String offlineData = settings.getString(Constants.getUserDataPreference(), "");
                        Preferencias[] p = gson.fromJson(offlineData, Preferencias[].class);
                        ArrayList<Preferencias> list = new ArrayList<Preferencias>(Arrays.asList(p));
                        createSpinner(list);
                        criticalPointButton.setEnabled(true);
                    }
                });

                criticalPointButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (preferencesSpinner.getSelectedItem() == null) {
                            errorTextView.setText("Por favor, selecione um obstáculo!");
                        } else {
                            errorTextView.setText(errorTextView.getText() + selectedItem);
                            pbar.setVisibility(View.VISIBLE);
                            final AndePUCRSAPI webService = app.getService();


                            /**
                             * Send PontoUsuario, first find the user point id with find all
                             * */
                            webService.findAllPoints(new Callback<ArrayList<Ponto>>() {
                                @Override
                                public void success(ArrayList<Ponto> pontos, Response response) {
                                    boolean pontoCadastrado = false;
                                    Ponto pReady = new Ponto();
                                    for (Ponto p : pontos) {
                                        if (p.getLatitude() == latLng.latitude && p.getLongitude() == latLng.longitude) {
                                            pontoCadastrado = true;
                                            pReady = p;
                                        }
                                    }
                                    /**
                                     * If Point is already on the server, send user point
                                     * */
                                    if (pontoCadastrado) {
                                        pontoUsuario.setPontoUsuarioPK(new PontoUsuarioPK(pReady.getNroIntPonto(), Integer.parseInt(String.valueOf(userID))));
                                        pontoUsuario.setUsuario(new Usuario(Integer.parseInt(String.valueOf(userID))));
                                        pontoUsuario.setPonto(pReady);
                                        pontoUsuario.setComentario(commentEditText.getText().toString());
                                        webService.createUserPoint(pontoUsuario, new Callback<PontoUsuario>() {
                                            @Override
                                            public void success(PontoUsuario pontoUsuario, Response response) {
                                                Toast.makeText(CriticalPointActivity.this, "Ponto U cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(CriticalPointActivity.this, MapsActivity.class);
                                                startActivity(i);
                                                pbar.setVisibility(View.INVISIBLE);
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {
                                                pbar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(CriticalPointActivity.this, "Ponto Já cadastrado", Toast.LENGTH_SHORT).show();
                                                Log.e(Constants.getAppName() + "Send USERPOINT", error.toString());
                                            }
                                        });
                                    }

                                    /**
                                     * Send point and letter user point
                                     * */
                                    else {
                                        Ponto p;
                                        Preferencias prefSelected = findPreference(selectedItem);
                                        p = new Ponto("Critico", "revisao", latLng.latitude, latLng.longitude, prefSelected);
                                        webService.createPoint(p, new Callback<Ponto>() {
                                            @Override
                                            public void success(Ponto ponto, Response response) {
                                                Toast.makeText(CriticalPointActivity.this, "Ponto cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                                                webService.findAllPoints(new Callback<ArrayList<Ponto>>() {
                                                    @Override
                                                    public void success(ArrayList<Ponto> pontos, Response response) {
                                                        for (Ponto p : pontos) {
                                                            if (p.getLatitude() == latLng.latitude && p.getLongitude() == latLng.longitude) {
                                                                pontoUsuario.setPontoUsuarioPK(new PontoUsuarioPK(p.getNroIntPonto(), Integer.parseInt(String.valueOf(userID))));
                                                                pontoUsuario.setUsuario(new Usuario(Integer.parseInt(String.valueOf(userID))));
                                                                pontoUsuario.setPonto(p);
                                                                pontoUsuario.setComentario(commentEditText.getText().toString());
                                                                webService.createUserPoint(pontoUsuario, new Callback<PontoUsuario>() {
                                                                    @Override
                                                                    public void success(PontoUsuario pontoUsuario, Response response) {
                                                                        Toast.makeText(CriticalPointActivity.this, "Ponto do U cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                                                                        Intent i = new Intent(CriticalPointActivity.this, MapsActivity.class);
                                                                        startActivity(i);
                                                                        pbar.setVisibility(View.INVISIBLE);
                                                                    }

                                                                    @Override
                                                                    public void failure(RetrofitError error) {
                                                                        pbar.setVisibility(View.INVISIBLE);
                                                                        Toast.makeText(CriticalPointActivity.this, "Falha, contate o ADM", Toast.LENGTH_SHORT).show();
                                                                        Log.e(Constants.getAppName() + "Send USERPOINT", error.toString());
                                                                    }
                                                                });
                                                                break;
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void failure(RetrofitError error) {
                                                        pbar.setVisibility(View.INVISIBLE);
                                                        Toast.makeText(CriticalPointActivity.this, "Servidor não encontrado", Toast.LENGTH_SHORT).show();
                                                        Log.e(Constants.getAppName() + "Send Point", error.toString());
                                                    }
                                                });
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {
                                                pbar.setVisibility(View.INVISIBLE);

                                                Log.e(Constants.getAppName() + "Send Point", error.toString());
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    pbar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(CriticalPointActivity.this, "Servidor não encontrado", Toast.LENGTH_SHORT).show();
                                    Log.e(Constants.getAppName() + "READ ALL POINTS", error.toString());
                                }
                            });
                        }
                    }
                });
            } catch (NumberFormatException n) {
                Log.e(TAG, "Error to parse double: " + pointLat + "#" + pointLong);
            }
        }
    }

    private Preferencias findPreference(String pref) {
        Gson gson = new Gson();
        String offlineData = settings.getString(Constants.getUserDataPreference(), "");
        Preferencias[] p = gson.fromJson(offlineData, Preferencias[].class);
        ArrayList<Preferencias> list = new ArrayList<Preferencias>(Arrays.asList(p));
        for (Preferencias preferencef : list) {
            if (preferencef.getNome().equals(pref)) {
                return preferencef;
            }
        }
        return null;
    }


    private void createSpinner(ArrayList<Preferencias> pref) {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for (Preferencias p : pref) {
            categories.add(p.getNome());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        preferencesSpinner.setAdapter(dataAdapter);
        pbar.setVisibility(View.INVISIBLE);
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
            i = new Intent(CriticalPointActivity.this, MapsActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_profile) {
            i = new Intent(CriticalPointActivity.this, ProfileSetupActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_search) {
            i = new Intent(CriticalPointActivity.this, SearchActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedItem = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(parent.getContext(), "Selecione um obstáculo: ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(CriticalPointActivity.this, SearchActivity.class);
        startActivity(i);
    }
}