package com.pucrs.andepucrs.api;

import com.pucrs.andepucrs.model.Comentario;
import com.pucrs.andepucrs.model.Estabelecimentos;
import com.pucrs.andepucrs.model.Map;
import com.pucrs.andepucrs.model.Ponto;
import com.pucrs.andepucrs.model.PontoUsuario;
import com.pucrs.andepucrs.model.Preferencias;
import com.pucrs.andepucrs.model.Usuario;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface AndePUCRSAPI {

    @POST("/andePuc/webresources/com.andepuc.usuario")
    void createUser(@Body Usuario usuario, Callback<Usuario> response);

    @GET("/andePuc/webresources/com.andepuc.usuario")
    void findAllUser(Callback<ArrayList<Usuario>> response);

    @GET("/andePuc/webresources/com.andepuc.preferencias")
    void findAllPreferences(Callback<ArrayList<Preferencias>> response);

    @POST("/andePuc/webresources/com.andepuc.ponto")
    void createPoint(@Body Ponto ponto, Callback<Ponto> response);

    @GET("/andePuc/webresources/com.andepuc.ponto")
    void findAllPoints(Callback<ArrayList<Ponto>> response);

    @GET("/andePuc/webresources/com.andepuc.estabelecimentos")
    void findAllLocations(Callback<ArrayList<Estabelecimentos>> response);

    @POST("/andePuc/webresources/com.andepuc.pontousuario")
    void createUserPoint(@Body PontoUsuario pontoUsuario, Callback<PontoUsuario> response);

    //@POST("/AndePUCRS-WebService-DBv4/webresources/com.andepucrs.ws.comentario")
    @POST("/andePuc/webresources/com.andepuc.comentario")
    void sendComment(@Body Comentario comment, Callback<Comentario> response);

    @GET("/andePuc/webresources/com.andepuc.map/{from}/{to}")
    void getMapFromTo(@Path("from")Integer from, @Path("to")Integer to, Callback<ArrayList<Map>> callback);

    @PUT("/andePuc/webresources/com.andepuc.usuario/{id}")
    void editUser(@Path("id")Integer id,@Body Usuario entity, Callback<ArrayList<Map>> callback);

}
