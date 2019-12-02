package com.example.eloquentequipos.model.rest;

import com.example.eloquentequipos.model.data.Equipo;
import com.example.eloquentequipos.model.data.Jugador;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface Client {

    //EQUIPOS

    @DELETE("equipo/{id}")
    Call<Integer> deleteEquipo(@Path("id") long id);

    @GET("equipo/{id}")
    Call<Equipo> getEquipo(@Path("id") long id);

    @GET("equipo")
    Call<ArrayList<Equipo>> getEquipos();

    @POST("equipo")
    Call<Long> postEquipo(@Body Equipo equipo);

    @PUT("equipo/{id}")
    Call<Equipo> putEquipo(@Path("id") long id, @Body Equipo equipo);

    //JUGADORES

    @DELETE("jugadores/{id}")
    Call<Integer> deleteJugadores(@Path("id") long id);

    @GET("jugadores/{id}")
    Call<Jugador> getJugadores(@Path("id") long id);

    @GET("jugadores")
    Call<ArrayList<Jugador>> getJugadores();

    @POST("jugador")
    Call<Long> postJugadores(@Body Jugador jugador);

    @PUT("jugador/{id}")
    Call<Jugador> putJugadores(@Path("id") long id, @Body Jugador jugador);

    //IMÁGENES

    @Multipart
    @POST("upload")
    Call<String> fileUpload(@Part MultipartBody.Part file);
}