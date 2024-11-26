package com.example.mi_app_habitos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mi_app_habitos.modelo.Evento;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class agregar extends AppCompatActivity {
    private String startDate = "";
    private String endDate = "";
    private Evento evento;
    private EditText editTextNombre, editTextDescripcion;
    private MaterialButton buttonOptions;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar);

        // Inicializar campos del formulario
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        buttonOptions = findViewById(R.id.button_options);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Configurar listeners para fechas y frecuencia
        setupDateAndTimePickers();
        setupFrequencyPicker();

        // Guardar datos al hacer clic en el botón
        btnGuardar.setOnClickListener(v -> guardarEvento());
    }

    private void guardarEvento() {
        // Obtener los valores de los campos de texto
        String nombre = editTextNombre.getText().toString().trim();
        String descripcion = editTextDescripcion.getText().toString().trim();
        String frecuencia = buttonOptions.getText().toString().trim();

        // Validar los datos del formulario
        if (nombre.isEmpty() || descripcion.isEmpty() || startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el objeto Evento
        Evento evento = new Evento();
        evento.setNombre(nombre);
        evento.setDescripcion(descripcion);
        evento.setFrecuencia(frecuencia);
        evento.setFecha_inicio(startDate); // Correcto: ya es un String
        evento.setFecha_final(endDate);   // Correcto: ya es un String

        // Llamar al API para guardar el evento
        RetrofitClient.getClient()
                .create(ApiService.class)
                .createEvento(evento)
                .enqueue(new Callback<Evento>() {
                    @Override
                    public void onResponse(Call<Evento> call, Response<Evento> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(agregar.this, "Evento guardado con éxito", Toast.LENGTH_SHORT).show();
                            finish(); // Cerrar la actividad después de guardar
                        } else {
                            Toast.makeText(agregar.this, "Error al guardar el evento", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Evento> call, Throwable t) {
                        Toast.makeText(agregar.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void setupDateAndTimePickers() {
        Button buttonStartDate = findViewById(R.id.button_start_date);
        Button buttonEndDate = findViewById(R.id.button_end_date);
        TextView textViewDateRange = findViewById(R.id.text_view_date_range);

        buttonStartDate.setOnClickListener(view -> showDatePickerDialog(date -> {
            startDate = date;
            updateDateRange(textViewDateRange);
        }));

        buttonEndDate.setOnClickListener(view -> showDatePickerDialog(date -> {
            endDate = date;
            updateDateRange(textViewDateRange);
        }));
    }

    private void showDatePickerDialog(OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, day1) -> {
            // Aquí cambiamos el formato a yyyy-MM-dd
            String selectedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, day1);
            listener.onDateSelected(selectedDate);  // Pasamos la fecha en el formato correcto
        }, year, month, day);

        datePickerDialog.show();
    }


    private void updateDateRange(TextView textView) {
        if (!startDate.isEmpty() && !endDate.isEmpty()) {
            textView.setText("Rango de Fechas: " + startDate + " - " + endDate);
        }
    }

    private void setupFrequencyPicker() {
        buttonOptions.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(agregar.this, view);
            popupMenu.getMenu().add("Diario");
            popupMenu.getMenu().add("Semanal");
            popupMenu.getMenu().add("Mensualmente");

            popupMenu.setOnMenuItemClickListener(item -> {
                String selectedOption = item.getTitle().toString();
                buttonOptions.setText(selectedOption);
                Toast.makeText(agregar.this, "Seleccionado: " + selectedOption, Toast.LENGTH_SHORT).show();
                return true;
            });

            popupMenu.show();
        });
    }

    private interface OnDateSelectedListener {
        void onDateSelected(String date);
    }
}
