package com.pucrs.andepucrs.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pucrs.andepucrs.R;
import com.pucrs.andepucrs.api.Constants;
import com.pucrs.andepucrs.model.Estabelecimentos;
import com.pucrs.andepucrs.model.Favorite;
import com.pucrs.andepucrs.model.Preferencias;

import java.util.ArrayList;

public class FavoriteDetailsActivity extends AppCompatActivity {
    private SharedPreferences settings;
    private TextView toTextView;
    private TextView fromTextView;
    private Button reDoRoute;
    MyCustomAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_details);
        settings = getSharedPreferences(Constants.getMyPreferenceFile(), 0);

        toTextView = (TextView) findViewById(R.id.toFavTextView);
        fromTextView = (TextView) findViewById(R.id.fromFavTextView);
        reDoRoute = (Button) findViewById(R.id.reDoRouteButton);

        Gson gson = new Gson();
        String offline = settings.getString(Constants.getFavoriteDetails(), "");
        final Favorite favorite = gson.fromJson(offline, Favorite.class);
        displayListView(favorite.getPreferencias());
        toTextView.setText("Para: " + favorite.getFinish().getNome());
        fromTextView.setText("De: "+favorite.getStart().toString());

        reDoRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Estabelecimentos e = favorite.getFinish();
                String searchPoint = gson.toJson(e);
                settings.edit().putString(Constants.getSerachPoint(), searchPoint).commit();
                Intent i = new Intent(FavoriteDetailsActivity.this, MapsActivity.class);
                i.putExtra("FromMenu", false);
                startActivity(i);
            }
        });
    }



    private void displayListView(ArrayList<Preferencias> preferenciasList) {
        for (Preferencias p : preferenciasList) {
            p.setSelected(p.isSelected());
        }
        dataAdapter = new MyCustomAdapter(this,
                R.layout.preferences_info, preferenciasList);
        ListView listView = (ListView) findViewById(R.id.prefListView);
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
            i = new Intent(FavoriteDetailsActivity.this, MapsActivity.class);
            i.putExtra("FromMenu", true);
            startActivity(i);
        }
        if (id == R.id.action_profile) {
            i = new Intent(FavoriteDetailsActivity.this, ProfileSetupActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_search) {
            i = new Intent(FavoriteDetailsActivity.this, SearchActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_favorite) {
            i = new Intent(FavoriteDetailsActivity.this, FavoriteActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyCustomAdapter extends ArrayAdapter<Preferencias> {

        private ArrayList<Preferencias> preferenciasList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Preferencias> preferenciasList) {
            super(context, textViewResourceId, preferenciasList);
            this.preferenciasList = new ArrayList<>();
            this.preferenciasList.addAll(preferenciasList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            //Log.v("ConvertView", String.valueOf(position));

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
            holder.name.setEnabled(false);
            holder.name.setTag(Preferencias);
            return convertView;

        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }
    }
}
