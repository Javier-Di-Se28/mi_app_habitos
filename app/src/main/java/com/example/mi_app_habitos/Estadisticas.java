package com.example.mi_app_habitos;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mi_app_habitos.adapter.ProgressAdapter;
import com.example.mi_app_habitos.modelo.Evento;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Estadisticas extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        // Configura RecyclerView
        recyclerView = findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Llamar al método para cargar los datos desde la API
        cargarDatosDesdeApi();
    }

    private void cargarDatosDesdeApi() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Evento>> call = apiService.getEventos();

        call.enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Evento> eventos = response.body();
                    adapter = new ProgressAdapter(eventos);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(Estadisticas.this, "No se pudieron cargar los eventos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(Estadisticas.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
