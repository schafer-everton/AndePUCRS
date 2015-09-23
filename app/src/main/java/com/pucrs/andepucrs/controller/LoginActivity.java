package com.pucrs.andepucrs.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pucrs.andepucrs.R;
import com.pucrs.andepucrs.api.AndePUCRSAPI;
import com.pucrs.andepucrs.api.Constants;
import com.pucrs.andepucrs.model.Usuario;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences settings;
    ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
    private EditText password;
    private EditText email;
    private Button login;
    private ProgressBar pbar;
    private boolean l;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        settings = getSharedPreferences(Constants.getMyPreferenceFile(), 0);
        login = (Button) findViewById(R.id.loginButton);
        password = (EditText) findViewById(R.id.passwordLoginEditText);
        email = (EditText) findViewById(R.id.emailLoginEditText);
        pbar = (ProgressBar) findViewById(R.id.progressBarLogin);
        pbar.setVisibility(View.INVISIBLE);
        l = false;
        Intent intent = getIntent();
        final String redirect = intent.getStringExtra(Constants.getRedirect());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbar.setVisibility(View.VISIBLE);
                retrofit.RestAdapter restAdapter = new retrofit.RestAdapter.Builder()
                        .setEndpoint(Constants.getServerURL()).build();
                AndePUCRSAPI api = restAdapter.create(AndePUCRSAPI.class);
                api.findAllUser(new Callback<ArrayList<Usuario>>() {
                    @Override
                    public void success(ArrayList<Usuario> usuarios, Response response) {
                        for (Usuario u : usuarios) {
                            if (u.getEmail().equals(email.getText().toString()) && u.getHashSenha().equals(password.getText().toString())) {
                                Toast.makeText(LoginActivity.this, "Login realizado com Sucesso", Toast.LENGTH_SHORT).show();
                                pbar.setVisibility(View.INVISIBLE);
                                /**
                                 * Save session
                                 * Save id
                                 * */
                                settings.edit().putBoolean(Constants.getSession(), true).commit();
                                settings.edit().putInt(Constants.getUserId(), u.getNroIntUsuario()).commit();
                                Gson gson = new Gson();
                                String offline = gson.toJson(u);
                                settings.edit().putString(Constants.getUser(), offline).commit();
                                l = true;
                                Intent i;
                                if (redirect.equals("CriticalPointActivity")) {
                                    i = new Intent(LoginActivity.this, CriticalPointActivity.class);
                                }else if(redirect.equals("CommentActivity")){
                                    i = new Intent(LoginActivity.this, CommentActivity.class);
                                }
                                else {
                                    i = new Intent(LoginActivity.this, SearchActivity.class);
                                }
                                startActivity(i);
                            }
                        }
                        if (!l) {
                            pbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "Servidor não encontrado", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
            i = new Intent(LoginActivity.this, MapsActivity.class);
            i.putExtra("FromMenu",true);
            startActivity(i);
        }
        if (id == R.id.action_profile) {
            i = new Intent(LoginActivity.this, ProfileSetupActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_search) {
            i = new Intent(LoginActivity.this, SearchActivity.class);
            startActivity(i);
        }
        if(id == R.id.action_favorite){
            i = new Intent(LoginActivity.this, FavoriteActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(LoginActivity.this, SearchActivity.class);
        startActivity(i);
    }
}
