package com.example.mi_app_habitos;

import com.example.mi_app_habitos.modelo.Evento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    // Definir la ruta para obtener los eventos (GET)
    @GET("eventos")
    Call<List<Evento>> getEventos();

    // Definir la ruta para crear un evento (POST)
    @POST("eventos")
    Call<Evento> createEvento(@Body Evento evento);

    // Definir la ruta para obtener un libro por ID (GET)
    @GET("eventos/{id}")
    Call<Evento> getEventoById(@Path("id") int id);

    // Definir la ruta para actualizar un libro (PUT)
    @PUT("eventos/{id}")
    Call<Evento> updateEvento(@Path("id") int id, @Body Evento evento);

    // Definir la ruta para eliminar un evento (DELETE)
    @DELETE("eventos/{id}")
    Call<Void> deleteEvento(@Path("id") int id);
}
