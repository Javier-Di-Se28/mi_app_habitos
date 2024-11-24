package com.example.mi_app_habitos;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mi_app_habitos.adapter.EventoAdapter;
import com.example.mi_app_habitos.modelo.Evento;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        recyclerView = findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cargarEventos();
    }

    private void cargarEventos() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        apiService.getEventos().enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Evento> eventos = response.body();

                    recyclerView.setAdapter(adapter);
                    Toast.makeText(EventoActivity.this, "Conexión exitosa", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EventoActivity.this, "Error en la respuesta: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(EventoActivity.this, "Error de comunicación: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
