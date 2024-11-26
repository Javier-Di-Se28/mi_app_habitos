package com.example.mi_app_habitos;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.mi_app_habitos.R.id.nav_view;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mi_app_habitos.adapter.EventoAdapter;
import com.example.mi_app_habitos.modelo.Evento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton floatingActionButton;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    // API
    private EventoAdapter adapter;
    private ApiService apiService;

    private List<Evento> eventos = new ArrayList<>();
    private List<Evento> eventosFiltrados = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerViewEventos);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
           // Inicializar Retrofit y ApiService
        apiService = RetrofitClient.getClient().create(ApiService.class);

        eventos = new ArrayList<>();
        eventosFiltrados = new ArrayList<>(); // Inicializar la lista de filtrados
        recyclerView.setAdapter(adapter);

        cargarEventos();

        eventosFiltrados.addAll(eventos); // Copiar eventos originales a filtrados

        adapter = new EventoAdapter(eventosFiltrados,this,apiService);
        recyclerView.setAdapter(adapter);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, agregar.class)));
    }

    private void cargarEventos() {

        RetrofitClient.getClient()
                .create(ApiService.class)
                .getEventos()
                .enqueue(new retrofit2.Callback<List<Evento>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Evento>> call, retrofit2.Response<List<Evento>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            eventos.clear();
                            eventosFiltrados.clear();

                            eventos.addAll(response.body());
                            eventosFiltrados.addAll(eventos); // Copia los eventos cargados a la lista filtrada
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<Evento>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error de conexión", LENGTH_SHORT).show();
                    }
                });
    }
    private void eliminarEvento(int position) {
        if (position < 0 || position >= eventos.size()) {
            Toast.makeText(MainActivity.this, "Posición inválida", Toast.LENGTH_SHORT).show();
            return;
        }

        Evento evento = eventos.get(position);

        // Llamar al método DELETE de la API
        apiService.deleteEvento(evento.getId()).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    // Eliminar el evento de la lista local y notificar al adaptador
                    eventos.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(MainActivity.this, "Evento eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error al eliminar el evento", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });

    }




    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.hogar) {
            // Handle "Hogar" menu item
        } else if (id == R.id.nav_salir) {
            mAuth.signOut();
            finish();
            startActivity(new Intent(MainActivity.this, login.class));
        } else if (id == R.id.Usuario) {
            Toast.makeText(this, "Perfil", LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
        } else if (id == R.id.nav_info) {
            Toast.makeText(this, "Estadísticas", LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, Estadisticas.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtsearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtsearch(query);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private void txtsearch(String str) {
        eventosFiltrados.clear();

        if (str.isEmpty()) {
            eventosFiltrados.addAll(eventos);
        } else {
            for (Evento evento : eventos) {

                String query = str.toLowerCase();
                String nombre = evento.getNombre().toLowerCase();
                String descripcion = evento.getDescripcion().toLowerCase();
                String frecuencia = evento.getFrecuencia().toLowerCase();
                String fechaInicio = evento.getFecha_inicio().toLowerCase();
                String fechaFin = evento.getFecha_final().toLowerCase();


                if (nombre.contains(query) ||
                        descripcion.contains(query) ||
                        frecuencia.contains(query) ||
                        fechaInicio.contains(query) ||
                        fechaFin.contains(query)) {
                    eventosFiltrados.add(evento);
                }
            }
        }

        adapter.notifyDataSetChanged(); // Actualiza el adaptador
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarEventos(); // Recargar la lista de eventos
    }
}
