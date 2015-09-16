package com.pucrs.andepucrs.api;

import com.pucrs.andepucrs.model.Estabelecimentos;
import com.pucrs.andepucrs.model.Ponto;
import com.pucrs.andepucrs.model.Preferencias;
import com.pucrs.andepucrs.model.Usuario;
import com.pucrs.andepucrs.model.PontoUsuario;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by ScHaFeR on 24/08/2015.
 */
public interface AndePUCRSAPI {

    @POST("/AndePUCRS-WebService-DBv3/webresources/com.andepuc.usuario")
    void createUser(@Body Usuario usuario, Callback<Usuario> response);

    @GET("/AndePUCRS-WebService-DBv3/webresources/com.andepuc.usuario")
    void findAllUser(Callback<ArrayList<Usuario>> response);

    @GET("/AndePUCRS-WebService-DBv3/webresources/com.andepuc.preferencias")
    void findAllPreferences(Callback<ArrayList<Preferencias>> response);

    @POST("/AndePUCRS-WebService-DBv3/webresources/com.andepuc.ponto")
    void createPoint(@Body Ponto ponto, Callback<Ponto> response);

    @GET("/AndePUCRS-WebService-DBv3/webresources/com.andepuc.ponto")
    void findAllPoints(Callback<ArrayList<Ponto>> response);

    @GET("/AndePUCRS-WebService-DBv3/webresources/com.andepuc.estabelecimentos")
    void findAllLocations(Callback<ArrayList<Estabelecimentos>> response);

    @POST("/AndePUCRS-WebService-DBv3/webresources/com.andepuc.pontousuario")
    void createUserPoint(@Body PontoUsuario pontoUsuario, Callback<PontoUsuario> response);
}
