package com.pucrs.andepucrs.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pucrs.andepucrs.AndePUCRSApplication;
import com.pucrs.andepucrs.R;
import com.pucrs.andepucrs.api.AndePUCRSAPI;
import com.pucrs.andepucrs.api.Constants;
import com.pucrs.andepucrs.model.Preferencias;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileSetupActivity extends AppCompatActivity {

    TextView profileTextView;
    Button saveProfile;
    SharedPreferences settings;
    MyCustomAdapter dataAdapter = null;
    private AndePUCRSApplication app;
    ProgressBar pbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        final Button myButton = (Button) findViewById(R.id.findSelected);
        settings = getSharedPreferences(Constants.getMyPreferenceFile(), 0);
        pbar = (ProgressBar) findViewById(R.id.prefProgressBar);
        pbar.setVisibility(View.VISIBLE);

        myButton.setEnabled(false);
        app = (AndePUCRSApplication) getApplication();
        AndePUCRSAPI api = app.getService();
        api.findAllPreferences(new Callback<ArrayList<Preferencias>>() {
            @Override
            public void success(ArrayList<Preferencias> preferenciases, Response response) {
                displayListView(preferenciases);
                checkButtonClick();
                pbar.setVisibility(View.INVISIBLE);
                myButton.setEnabled(true);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(Constants.getAppName(), "Falha ao buscar pref do server");
                Gson gson = new Gson();
                String offlineData = settings.getString(Constants.getUserDataPreference(), "");
                Preferencias[] p = gson.fromJson(offlineData, Preferencias[].class);
                ArrayList<Preferencias> list = new ArrayList<Preferencias>(Arrays.asList(p));
                displayListView(list);
                checkButtonClick();
                pbar.setVisibility(View.INVISIBLE);
                myButton.setEnabled(true);
            }
        });


    }

    private boolean isSelected(int id){
        Gson gson = new Gson();
        String offlineData = settings.getString(Constants.getUserDataPreference(), "");
        Preferencias[] p = gson.fromJson(offlineData, Preferencias[].class);
        if(p != null){
            ArrayList<Preferencias> list = new ArrayList<Preferencias>(Arrays.asList(p));
            for (Preferencias p1: list){
                if(id == p1.getNroIntPref()){

                    return p1.isSelected();
                }
            }
            return false;
        }
        return false;
    }

    private void displayListView(ArrayList<Preferencias> preferenciasList) {
        for (Preferencias p: preferenciasList){
            p.setSelected(isSelected(p.getNroIntPref()));
        }
        dataAdapter = new MyCustomAdapter(this,
                R.layout.preferences_info, preferenciasList);
        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(dataAdapter);
    }

    private void checkButtonClick() {
        Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int selectCount = 0;
                StringBuffer responseText = new StringBuffer();
                responseText.append(R.string.preferencias_selecionadas + "... \n");
                ArrayList<Preferencias> preferenciasList = dataAdapter.preferenciasList;

                for (int i = 0; i < preferenciasList.size(); i++) {
                    Preferencias Preferencias = preferenciasList.get(i);
                    if (Preferencias.isSelected()) {
                        selectCount++;
                        responseText.append("\n").append(Preferencias.toString());
                    }
                }
                Toast.makeText(ProfileSetupActivity.this, "Sucesso", Toast.LENGTH_SHORT).show();
                if (selectCount == 0) {
                    Toast.makeText(ProfileSetupActivity.this, "Por favor, selecione ao menos uma obstáculo", Toast.LENGTH_SHORT).show();
                } else {
                    /**
                     * Save 1st time (end configuration section)
                     * */
                    settings.edit().putBoolean(Constants.getFirstTime(), false).commit();

                    /**
                     * Save data offline
                     * */
                    Gson gson = new Gson();
                    String offlineData = gson.toJson(preferenciasList);
                    settings.edit().putString(Constants.getUserDataPreference(), offlineData).commit();
                    Intent i = new Intent(ProfileSetupActivity.this, SearchActivity.class);
                    startActivity(i);
                }
            }

        });
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(ProfileSetupActivity.this, SearchActivity.class);
        startActivity(i);
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
        if(id == R.id.action_maps){
            i = new Intent(ProfileSetupActivity.this, MapsActivity.class);
            startActivity(i);
        }
        if(id == R.id.action_profile){
            i = new Intent(ProfileSetupActivity.this, ProfileSetupActivity.class);
            startActivity(i);
        }
        if(id == R.id.action_search){
            i = new Intent(ProfileSetupActivity.this, SearchActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyCustomAdapter extends ArrayAdapter<Preferencias> {

        private ArrayList<Preferencias> preferenciasList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Preferencias> preferenciasList) {
            super(context, textViewResourceId, preferenciasList);
            this.preferenciasList = new ArrayList<Preferencias>();
            this.preferenciasList.addAll(preferenciasList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.preferences_info, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);


                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Preferencias Preferencias = (Preferencias) cb.getTag();
                        Preferencias.setSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Preferencias Preferencias = preferenciasList.get(position);
            holder.code.setText(" (" + Preferencias.getNroIntPref() + ")");
            holder.name.setText(Preferencias.getNome());
            holder.name.setChecked(Preferencias.isSelected());
            holder.name.setTag(Preferencias);
            return convertView;

        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

    }
}