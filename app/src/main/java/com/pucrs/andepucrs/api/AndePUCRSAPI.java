package com.pucrs.andepucrs.api;

import com.pucrs.andepucrs.model.Establishment;
import com.pucrs.andepucrs.model.Ponto;
import com.pucrs.andepucrs.model.Preference;
import com.pucrs.andepucrs.model.User;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by ScHaFeR on 24/08/2015.
 */
public interface AndePUCRSAPI {

    @POST("/com.andepuc.usuario")
    void createUser(@Body User user, Callback<User> response);

    @GET("/com.andepuc.usuario")
    void findAllUser(Callback<ArrayList<User>> response);

    @GET("/com.andepuc.preferencias")
    void findAllPreferences(Callback<ArrayList<Preference>> response);

    @POST("/com.andepuc.ponto")
    void createPoint(@Body Ponto ponto, Callback<Ponto> response);

    @GET("/com.andepuc.ponto")
    void findAllPoints(Callback<ArrayList<Ponto>> response);

    @GET("/com.andepuc.estabelecimentos")
    void findAllLocations(Callback<ArrayList<Establishment>> response);

}
