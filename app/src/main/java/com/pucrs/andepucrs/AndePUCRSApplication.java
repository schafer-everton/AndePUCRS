package com.pucrs.andepucrs;

import android.app.Application;

import com.pucrs.andepucrs.api.AndePUCRSAPI;
import com.pucrs.andepucrs.api.Constants;

/**
 * Created by ScHaFeR on 01/09/2015.
 */
public class AndePUCRSApplication extends Application {

    private AndePUCRSAPI webService;

    @Override
    public void onCreate() {
        super.onCreate();
        retrofit.RestAdapter restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(Constants.getServerURL()).build();
        webService = restAdapter.create(AndePUCRSAPI.class);
    }

    public AndePUCRSAPI getService(){
        return webService;
    }


}
