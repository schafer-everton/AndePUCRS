package com.pucrs.andepucrs.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.pucrs.andepucrs.model.Usuario;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignUpActivity extends AppCompatActivity {

    private ProgressBar pbar;
    private SharedPreferences settings;
    private Button signInButton;
    private EditText passwordEditText;
    private EditText passwordVerifyEditText;
    private EditText emailEditText;
    private EditText nameEditText;
    private TextView errorTextView;
    private int moveon;
    private AndePUCRSApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        moveon = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final SharedPreferences settings = getSharedPreferences(Constants.getMyPreferenceFile(), 0);

        signInButton = (Button) findViewById(R.id.signInButton);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordVerifyEditText = (EditText) findViewById(R.id.passwordVerifyEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        errorTextView = (TextView) findViewById(R.id.errorTextView);
        pbar = (ProgressBar) findViewById(R.id.progressBarSignin);
        pbar.setVisibility(View.INVISIBLE);
        errorTextView.setText("");
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveon = 0;
                if (passwordVerifyEditText.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.dados_cadastro,
                            Toast.LENGTH_LONG).show();
                    moveon = 1;

                    passwordEditText.requestFocus();
                }

                if (emailEditText.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.dados_cadastro,
                            Toast.LENGTH_LONG).show();
                    moveon = 1;
                    emailEditText.requestFocus();
                }
                if (nameEditText.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.dados_cadastro,
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

                if (moveon == 0) {
                    pbar.setVisibility(View.VISIBLE);

                    app = (AndePUCRSApplication) getApplication();
                    AndePUCRSAPI api = app.getService();

                    Usuario usuario = new Usuario(nameEditText.getText().toString(),
                            emailEditText.getText().toString(),
                            passwordEditText.getText().toString());

                    Log.d(Constants.getAppName(), usuario.toString());
                    api.createUser(usuario,
                            new Callback<Usuario>() {
                                @Override
                                public void success(Usuario usuario, Response response) {
                                    pbar.setVisibility(View.INVISIBLE);
                                    app = (AndePUCRSApplication) getApplication();

                                    AndePUCRSAPI webService = app.getService();
                                    webService.findAllUser(new Callback<ArrayList<Usuario>>() {
                                        @Override
                                        public void success(ArrayList<Usuario> usuarios, Response response) {
                                            for (Usuario u : usuarios) {
                                                if (u.getEmail().equals(emailEditText.getText().toString())) {

                                                    settings.edit().putInt(Constants.getUserId(), u.getNroIntUsuario()).commit();
                                                    Gson gson = new Gson();
                                                    String offlineData = gson.toJson(u);
                                                    /**
                                                     * Save user id
                                                     * */
                                                    settings.edit().putString(Constants.getUserData(), offlineData).commit();
                                                    /**
                                                     * Save 1st time
                                                     * */
                                                    settings.edit().putBoolean(Constants.getFirstTime(), false).commit();

                                                    Intent i = new Intent(SignUpActivity.this, ProfileSetupActivity.class);
                                                    startActivity(i);
                                                }
                                            }
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            errorTextView.setText("READ" + error.toString());
                                        }
                                    });
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    pbar.setVisibility(View.INVISIBLE);
                                    if (error.getKind().toString().equals("NETWORK")) {
                                        Log.e(Constants.getAppName(), error.toString());
                                        errorTextView.setText("Servidor não encontrado, por favor verifique a sua conexão ou contate o administrador do AndePUCRS");
                                    } else if (error.getMessage().contains("500")) {
                                        errorTextView.setText("Usuário já cadastrado!");
                                        Log.e(Constants.getAppName(), error.toString());
                                    } else {
                                        errorTextView.setText(error.toString());
                                        Log.e(Constants.getAppName(), error.toString());
                                    }

                                }
                            });
                }
            }
        });
    }

    protected boolean validatePassword(String password, String passwordVerify) {
        return password.length() >= 6 && password.equals(passwordVerify);
    }

    protected boolean validateEmail(String email) {
        if (email.length() >= 5 && email.contains("@")) return true;
        return false;
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    /*

     @Override public boolean onCreateOptionsMenu(Menu menu) {
     // Inflate the menu; this adds items to the action bar if it is present.
     getMenuInflater().inflate(R.menu.menu_home, menu);
     return true;
     }

     @Override public boolean onOptionsItemSelected(MenuItem item) {
     int id = item.getItemId();
     if (id == R.id.action_settings) {
     return true;
     }
     return super.onOptionsItemSelected(item);
     }*/
}
