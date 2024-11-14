package com.example.mi_app_habitos;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.PopupMenu;
import com.google.android.material.button.MaterialButton;


public class agregar extends AppCompatActivity {
    private String startDate = "";
    private String endDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar);
        MaterialButton buttonOptions = findViewById(R.id.button_options);


        Button buttonStartDate = findViewById(R.id.button_start_date);
        Button buttonEndDate = findViewById(R.id.button_end_date);
        TextView textViewDateRange = findViewById(R.id.text_view_date_range);


        Button buttonTimePicker = findViewById(R.id.button_time_picker);
        TextView textViewSelectedTime = findViewById(R.id.text_view_selected_time);

        buttonStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog((date) -> {
                    startDate = date;
                    updateDateRange(textViewDateRange);
                });
            }
        });

        buttonEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog((date) -> {
                    endDate = date;
                    updateDateRange(textViewDateRange);
                });
            }
        });

        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener la hora actual
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Crear y mostrar el TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(agregar.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Actualizar el TextView con la hora seleccionada
                                String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                                textViewSelectedTime.setText("Hora seleccionada: " + selectedTime);
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(agregar.this, view);
                popupMenu.getMenu().add("Diario");
                popupMenu.getMenu().add("Semanal");
                popupMenu.getMenu().add("Mensualmente");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String selectedOption = item.getTitle().toString();
                        buttonOptions.setText(selectedOption); // Cambia el texto del botón a la opción seleccionada
                        Toast.makeText(agregar.this, "Seleccionado: " + selectedOption, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }
    private void showDatePickerDialog(OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
            String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
            listener.onDateSelected(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void updateDateRange(TextView textView) {
        if (!startDate.isEmpty() && !endDate.isEmpty()) {
            textView.setText("Rango de Fechas: " + startDate + " - " + endDate);
        }
    }

    private interface OnDateSelectedListener {
        void onDateSelected(String date);
    }
}