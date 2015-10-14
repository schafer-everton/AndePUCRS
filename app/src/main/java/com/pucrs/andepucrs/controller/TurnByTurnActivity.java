package com.pucrs.andepucrs.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.pucrs.andepucrs.R;
import com.pucrs.andepucrs.api.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class TurnByTurnActivity extends AppCompatActivity {

    private ListView listView;
    private SharedPreferences settings;
    private ImageButton ttsButton;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_by_turn);
        listView = (ListView) findViewById(R.id.turnListView);
        settings = getSharedPreferences(Constants.getMyPreferenceFile(), 0);
        /**
         * Read Seach points
         * */
        Gson gson = new Gson();
        String offlineData = settings.getString(Constants.getTurnByTurn(), "");
        String[] r = gson.fromJson(offlineData, String[].class);
        final ArrayList result = new ArrayList<>(Arrays.asList(r));
        createList(result);

        ttsButton=(ImageButton)findViewById(R.id.ttsButton);
        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.getDefault());
                }
            }
        });

        ttsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = result.toString();
                tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }
    public void onPause(){
        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }

    private void createList(ArrayList<String> pref) {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        for (String p : pref) {
            categories.add(p);
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
            i = new Intent(TurnByTurnActivity.this, MapsActivity.class);
            i.putExtra("FromMenu",true);
            startActivity(i);
        }
        if (id == R.id.action_profile) {
            i = new Intent(TurnByTurnActivity.this, ProfileSetupActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_search) {
            i = new Intent(TurnByTurnActivity.this, SearchActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_favorite) {
            i = new Intent(TurnByTurnActivity.this, FavoriteActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
