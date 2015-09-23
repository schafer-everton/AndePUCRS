package com.pucrs.andepucrs.api;

import com.pucrs.andepucrs.model.Comentario;
import com.pucrs.andepucrs.model.Estabelecimentos;
import com.pucrs.andepucrs.model.Ponto;
import com.pucrs.andepucrs.model.PontoUsuario;
import com.pucrs.andepucrs.model.Preferencias;
import com.pucrs.andepucrs.model.Usuario;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface AndePUCRSAPI {

    @POST("/AndePUCRS-WebService-DBv4/webresources/com.andepucrs.ws.usuario")

    void createUser(@Body Usuario usuario, Callback<Usuario> response);

    @GET("/AndePUCRS-WebService-DBv4/webresources/com.andepucrs.ws.usuario")
    void findAllUser(Callback<ArrayList<Usuario>> response);

    @GET("/AndePUCRS-WebService-DBv4/webresources/com.andepucrs.ws.preferencias")
    void findAllPreferences(Callback<ArrayList<Preferencias>> response);

    @POST("/AndePUCRS-WebService-DBv4/webresources/com.andepucrs.ws.ponto")
    void createPoint(@Body Ponto ponto, Callback<Ponto> response);

    @GET("/AndePUCRS-WebService-DBv4/webresources/com.andepucrs.ws.ponto")
    void findAllPoints(Callback<ArrayList<Ponto>> response);

    @GET("/AndePUCRS-WebService-DBv4/webresources/com.andepucrs.ws.estabelecimentos")
    void findAllLocations(Callback<ArrayList<Estabelecimentos>> response);

    @POST("/AndePUCRS-WebService-DBv4/webresources/com.andepucrs.ws.pontousuario")
    void createUserPoint(@Body PontoUsuario pontoUsuario, Callback<PontoUsuario> response);

    @POST("/AndePUCRS-WebService-DBv4/webresources/com.andepucrs.ws.comentario")
    void sendComment(@Body Comentario comment, Callback<Comentario> response);


}
