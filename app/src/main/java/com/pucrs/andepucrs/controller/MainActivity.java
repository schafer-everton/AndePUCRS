package com.pucrs.andepucrs.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pucrs.andepucrs.R;
import com.pucrs.andepucrs.api.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(Constants.getMyPreferenceFile(), 0);

        if (settings.getBoolean(Constants.getFirstTime(), true)) {
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(i);
        }
    }

    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
     */
    @Override
    public void onBackPressed() {
        finish();
    }
}
