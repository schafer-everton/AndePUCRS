package com.pucrs.andepucrs.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pucrs.andepucrs.AndePUCRSApplication;
import com.pucrs.andepucrs.R;
import com.pucrs.andepucrs.api.AndePUCRSAPI;
import com.pucrs.andepucrs.api.Constants;
import com.pucrs.andepucrs.model.Map;
import com.pucrs.andepucrs.model.Usuario;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserConfiguration extends AppCompatActivity {

    private ProgressBar pbar;
    private SharedPreferences settings;
    private Button signInButton;
    private EditText passwordEditText;
    private EditText passwordVerifyEditText;
    private EditText emailEditText;
    private EditText nameEditText;
    private EditText oldPasswordEditText;
    private TextView errorTextView;
    private int moveon;
    private AndePUCRSApplication app;
    private Usuario user;
    private ArrayList<Usuario> allUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_configuration);
        settings = getSharedPreferences(Constants.getMyPreferenceFile(), 0);
        signInButton = (Button) findViewById(R.id.signInButton);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordVerifyEditText = (EditText) findViewById(R.id.passwordVerifyEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        errorTextView = (TextView) findViewById(R.id.errorTextView);
        oldPasswordEditText = (EditText) findViewById(R.id.oldPasswordEditText);
        pbar = (ProgressBar) findViewById(R.id.progressBarSignin);
        pbar.setVisibility(View.INVISIBLE);
        errorTextView.setText("");

        Gson gson =  new Gson();
        String offlineData = settings.getString(Constants.getUserData(), "");
        user = gson.fromJson(offlineData, Usuario.class);

        nameEditText.setText(user.getNome());
        emailEditText.setText(user.getEmail());



        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveon = 0;
                pbar.setVisibility(View.VISIBLE);
                app = (AndePUCRSApplication) getApplication();
                final AndePUCRSAPI api = app.getService();
                api.findAllUser(new Callback<ArrayList<Usuario>>() {
                    @Override
                    public void success(ArrayList<Usuario> usuarios, Response response) {
                        allUser = usuarios;
                        if (passwordEditText.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "As senhas não podem ser vazias",
                                    Toast.LENGTH_LONG).show();
                            moveon = 1;
                            passwordEditText.requestFocus();
                        }
                        if (passwordVerifyEditText.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "As senhas não podem ser vazias",
                                    Toast.LENGTH_LONG).show();
                            moveon = 1;
                            passwordVerifyEditText.requestFocus();
                        }

                        if (emailEditText.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "O email não pode ser vazio",
                                    Toast.LENGTH_LONG).show();
                            moveon = 1;
                            emailEditText.requestFocus();
                        }
                        if (nameEditText.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(),"O nome não pode ser vazio",
                                    Toast.LENGTH_LONG).show();
                            moveon = 1;
                            nameEditText.requestFocus();
                        }

                        if (!validatePassword(passwordEditText.getText().toString(), passwordVerifyEditText.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "As senhas não conhecidem ou são menores que 6",
                                    Toast.LENGTH_LONG).show();
                            moveon = 1;
                            passwordEditText.requestFocus();
                        }
                        if (!validateEmail(emailEditText.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Informe um email válido", Toast.LENGTH_LONG).show();
                            moveon = 1;
                            emailEditText.requestFocus();
                        }
                        if (nameEditText.length() <= 0) {
                            Toast.makeText(getApplicationContext(), "Informe um nome válido", Toast.LENGTH_LONG).show();
                            moveon = 1;
                            nameEditText.requestFocus();
                        }
                        boolean hit=false;
                        for (Usuario u : usuarios){
                            if(emailEditText.getText().toString().equals(u.getEmail())){
                                if(oldPasswordEditText.getText().toString().equals(u.getHashSenha())){
                                    hit = true;
                                    user.setNroIntUsuario(u.getNroIntUsuario());
                                }
                            }
                        }
                        if (moveon == 0 && hit) {
                           // app = (AndePUCRSApplication) getApplication();

                            user.setNome(nameEditText.getText().toString());
                            user.setEmail(emailEditText.getText().toString());
                            user.setHashSenha(passwordEditText.getText().toString());
                           // AndePUCRSAPI api = app.getService();
                            api.editUser(user.getNroIntUsuario(), user, new Callback<ArrayList<Map>>() {
                                @Override
                                public void success(ArrayList<Map> maps, Response response) {
                                    pbar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(UserConfiguration.this, "Sucesso", Toast.LENGTH_SHORT).show();
                                    settings.edit().putInt(Constants.getUserId(), user.getNroIntUsuario()).commit();
                                    Gson gson = new Gson();
                                    String offlineData = gson.toJson(user);
                                    settings.edit().putString(Constants.getUserData(), offlineData).commit();
                                    settings.edit().putBoolean(Constants.getSession(), false).commit();
                                    Intent i = new Intent(UserConfiguration.this, SearchActivity.class);
                                    startActivity(i);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    pbar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(UserConfiguration.this, "Não foi possivel atualizar as informações, tente novamente mais tarde", Toast.LENGTH_SHORT).show();
                                    Log.d(Constants.getAppName(), error.getMessage().toString());
                                }
                            });

                        }else
                        {
                            pbar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        });

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
            i = new Intent(UserConfiguration.this, MapsActivity.class);
            i.putExtra("FromMenu", true);
            startActivity(i);
        }
        if (id == R.id.action_profile) {
            i = new Intent(UserConfiguration.this, ProfileSetupActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_search) {
            i = new Intent(UserConfiguration.this, SearchActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_favorite) {
            i = new Intent(UserConfiguration.this, FavoriteActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_user) {
            i = new Intent(UserConfiguration.this, UserConfiguration.class);
            startActivity(i);
        }
        if (id == R.id.action_logout) {
            if(settings.getBoolean(Constants.getSession(),false)){
                settings.edit().putBoolean(Constants.getSession(), false).commit();
                Toast.makeText(UserConfiguration.this, "Usuário desconectou", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(UserConfiguration.this, "Nenhum usuário conectado para fazer logoff", Toast.LENGTH_SHORT).show();
            }
            i = new Intent(UserConfiguration.this, SearchActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    protected boolean validatePassword(String password, String passwordVerify) {
        return password.length() >= 6 && password.equals(passwordVerify);
    }

    protected boolean validateEmail(String email) {
        if (email.length() >= 5 && email.contains("@")) return true;
        return false;
    }
}
