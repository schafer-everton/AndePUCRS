package com.pucrs.andepucrs.controller;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.pucrs.andepucrs.AndePUCRSApplication;
import com.pucrs.andepucrs.R;
import com.pucrs.andepucrs.api.AndePUCRSAPI;
import com.pucrs.andepucrs.api.Constants;
import com.pucrs.andepucrs.model.Comentario;
import com.pucrs.andepucrs.model.Estabelecimentos;
import com.pucrs.andepucrs.model.Usuario;

import java.util.ArrayList;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CommentActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private NumberPicker np;
    private int numberSelected;
    private EditText commentEditText;
    private Button sendComment;
    private SharedPreferences settings;
    private AndePUCRSApplication app;
    private TextView fromTextView;
    private TextView toTextView;
    private ImageButton btnSpeak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        settings = getSharedPreferences(Constants.getMyPreferenceFile(), 0);
        np = (NumberPicker) findViewById(R.id.numberPicker);
        commentEditText = (EditText) findViewById(R.id.commentEditText);
        sendComment = (Button) findViewById(R.id.sendCommentButton);
        fromTextView = (TextView) findViewById(R.id.fromTextView);
        toTextView = (TextView) findViewById(R.id.toTextView);

        btnSpeak = (ImageButton) findViewById(R.id.speakCommentButton);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        np.setMaxValue(5);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);

        np.setOrientation(LinearLayout.HORIZONTAL);

        final int userID = settings.getInt(Constants.getUserId(), 0);
        final Boolean isLoggedIn = settings.getBoolean(Constants.getSession(), false);

        if (!isLoggedIn) {
            Intent i = new Intent(CommentActivity.this, LoginActivity.class);
            i.putExtra(Constants.getRedirect(), "CommentActivity");
            startActivity(i);
        } else {
            app = (AndePUCRSApplication) getApplication();
            final AndePUCRSAPI webService = app.getService();
            /**
             * Read previous favorite routes
             * */
            Gson gson = new Gson();
            String offlineData = settings.getString(Constants.getSerachPoint(), "");
            final Estabelecimentos e = gson.fromJson(offlineData, Estabelecimentos.class);
            offlineData = settings.getString(Constants.getMyCurrentLocation(), "");
            final LatLng myCurrentLocation = gson.fromJson(offlineData, LatLng.class);
            offlineData = settings.getString(Constants.getUser(), "");
            final Usuario user = gson.fromJson(offlineData, Usuario.class);

            fromTextView.setText("De: " + myCurrentLocation.latitude + ", " + myCurrentLocation.longitude);
            toTextView.setText("Para: " + e.getNome());

            sendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commentEditText.getText().toString().equals("") || commentEditText.getText().toString() == null) {
                        commentEditText.requestFocus();
                        Toast.makeText(CommentActivity.this, "Informe os dados de comentário", Toast.LENGTH_SHORT).show();
                    } else {
                        webService.sendComment(new Comentario(myCurrentLocation.latitude, myCurrentLocation.longitude, e.getLatitude(), e.getLongitude(), commentEditText.getText().toString(), numberSelected, user), new Callback<Comentario>() {
                            @Override
                            public void success(Comentario comentario, Response response) {
                                Intent i = new Intent(CommentActivity.this, MapsActivity.class);
                                startActivity(i);
                                Toast.makeText(CommentActivity.this, "Comentário Enviado com sucesso!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Toast.makeText(CommentActivity.this, "Falha ao enviar o comentário, por favor, verifique a sua conexão", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });
        }


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
                    commentEditText.setText(result.get(0));
                }
                break;
            }

        }
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
            i = new Intent(CommentActivity.this, MapsActivity.class);
            i.putExtra("FromMenu", true);
            startActivity(i);
        }
        if (id == R.id.action_profile) {
            i = new Intent(CommentActivity.this, ProfileSetupActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_search) {
            i = new Intent(CommentActivity.this, SearchActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_favorite) {
            i = new Intent(CommentActivity.this, FavoriteActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        numberSelected = newVal;
    }
}
