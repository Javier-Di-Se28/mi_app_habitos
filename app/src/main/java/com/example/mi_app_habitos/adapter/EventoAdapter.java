package com.example.mi_app_habitos.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mi_app_habitos.ApiService;
import com.example.mi_app_habitos.R;
import com.example.mi_app_habitos.RetrofitClient;
import com.example.mi_app_habitos.modelo.Evento;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventoAdapter  extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {
    private List<Evento> eventos;
    private Context context;
    private ApiService apiService;
    private List<String> lista;

    public EventoAdapter(List<String> lista) {
        this.lista = lista;
    }

    public EventoAdapter(List<Evento> eventos, Context context, ApiService apiService) {
        this.eventos = eventos;
        this.context = context;
        this.apiService = apiService;
    }


    @NonNull
    @Override
    public EventoAdapter.EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_habitos, parent, false);
        return new EventoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoAdapter.EventoViewHolder holder, int position) {
        Evento evento = eventos.get(position);
        holder.nombre.setText(evento.getNombre());
        holder.descripcion.setText(evento.getDescripcion());
        holder.frecuencia.setText(evento.getFrecuencia());
        holder.fecha_inicio.setText(evento.getFecha_inicio());
        holder.fecha_final.setText(evento.getFecha_final());


        // Configurar el botón de edición
        holder.btnEdit.setText("Editar");
        holder.btnEdit.setOnClickListener(v -> {
            // Mostrar el formulario de edición
            showEditDialog(evento, position);
        });

        holder.btnDelete.setText("Eliminar");
        holder.btnDelete.setOnClickListener(v -> {
            eliminarEvento(position, evento.getId()); // Eliminar el evento
        });
    }

    private void showEditDialog(final Evento evento, final int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.update_poput, null);

        final EditText editTextNombre = dialogView.findViewById(R.id.editTextNombre);
        final EditText editTextDescripcion = dialogView.findViewById(R.id.editTextDescripcion);
        final EditText editTextFrecuencia = dialogView.findViewById(R.id.editTextFrecuencia);
        MaterialButton editTextFechaInicio = dialogView.findViewById(R.id.button_start_date);
        MaterialButton editTextFechaFinal = dialogView.findViewById(R.id.button_end_date);

        // Pre-llenar los campos con los datos del evento actual y evitar NullPointerException
        editTextNombre.setText(evento.getNombre() != null ? evento.getNombre() : "");
        editTextDescripcion.setText(evento.getDescripcion() != null ? evento.getDescripcion() : "");
        editTextFrecuencia.setText(evento.getFrecuencia() != null ? evento.getFrecuencia() : "");

        // Verificar si las fechas no son nulas antes de asignarlas
        if (evento.getFecha_inicio() != null) {
            editTextFechaInicio.setText(evento.getFecha_inicio());
        } else {
            editTextFechaInicio.setText("Fecha de inicio no disponible");
        }

        if (evento.getFecha_final() != null) {
            editTextFechaFinal.setText(evento.getFecha_final());
        } else {
            editTextFechaFinal.setText("Fecha final no disponible");
        }

        // Mostrar DatePicker al hacer clic en el botón de fecha de inicio
        editTextFechaInicio.setOnClickListener(v -> showDatePickerDialog(date -> {
            evento.setFecha_inicio(date); // Actualiza la fecha de inicio del evento
            editTextFechaInicio.setText(date); // Actualiza el texto en el botón
        }));

        // Mostrar DatePicker al hacer clic en el botón de fecha final
        editTextFechaFinal.setOnClickListener(v -> showDatePickerDialog(date -> {
            evento.setFecha_final(date); // Actualiza la fecha de fin del evento
            editTextFechaFinal.setText(date); // Actualiza el texto en el botón
        }));

        // Configurar el EditText de frecuencia para que funcione como un botón
        editTextFrecuencia.setFocusable(false);
        editTextFrecuencia.setClickable(true);

        // Mostrar el PopupMenu cuando se haga clic en el EditText de frecuencia

        editTextFrecuencia.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.getMenu().add("Diario");
            popupMenu.getMenu().add("Semanal");
            popupMenu.getMenu().add("Mensualmente");

            popupMenu.setOnMenuItemClickListener(item -> {
                String selectedOption = item.getTitle().toString();
                editTextFrecuencia.setText(selectedOption);  // Actualiza el texto del TextView con la opción seleccionada
                return true;
            });

            popupMenu.show();
        });

        // Crear el diálogo
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            // Obtener los datos del formulario
            String nombre = editTextNombre.getText().toString().trim();
            String descripcion = editTextDescripcion.getText().toString().trim();
            String frecuencia = editTextFrecuencia.getText().toString().trim();
            String fechaInicio = editTextFechaInicio.getText().toString().trim();
            String fechaFinal = editTextFechaFinal.getText().toString().trim();

            // Verificar si los campos están completos
            if (!nombre.isEmpty() && !descripcion.isEmpty() && !frecuencia.isEmpty() && !fechaInicio.isEmpty() && !fechaFinal.isEmpty()) {
                // Actualizar el evento localmente
                evento.setNombre(nombre);
                evento.setDescripcion(descripcion);
                evento.setFrecuencia(frecuencia);
                evento.setFecha_inicio(fechaInicio);
                evento.setFecha_final(fechaFinal);

                // Enviar los datos actualizados al servidor
                updateEvento(evento, position);
            } else {
                Toast.makeText(context, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    // Método para mostrar el DatePickerDialog
    private void showDatePickerDialog(OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year1, month1, day1) -> {
            // Aquí cambiamos el formato a yyyy-MM-dd
            String selectedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, day1);
            // Suponiendo que el método onDateSelected recibe la fecha en formato yyyy-MM-dd
            listener.onDateSelected(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }


    // Interface para escuchar la selección de fecha
    private interface OnDateSelectedListener {
        void onDateSelected(String date);
    }

    // Método para actualizar el evento en la base de datos
    private void updateEvento(Evento evento, int position) {
        RetrofitClient.getClient()
                .create(ApiService.class)
                .updateEvento(evento.getId(), evento)  // Enviar la ID del evento junto con los datos modificados
                .enqueue(new Callback<Evento>() {
                    @Override
                    public void onResponse(Call<Evento> call, Response<Evento> response) {
                        if (response.isSuccessful()) {
                            // Después de actualizar el evento en la base de datos, actualiza la interfaz
                            eventos.set(position, evento);  // Actualiza el evento en la lista de eventos
                            eventos.set(position, response.body());  // Actualiza el evento en la lista local
                            notifyItemChanged(position);  // Notifica al RecyclerView para actualizar el ítem
                            Toast.makeText(context, "Evento actualizado con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error al actualizar el evento", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Evento> call, Throwable t) {
                        Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public int getItemCount() {
        return eventos.size();
    }

    // Método para eliminar el evento usando Retrofit
    private void eliminarEvento(int position, int eventoId) {
        Call<Void> call = apiService.deleteEvento(eventoId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Eliminar evento de la lista
                    eventos.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Evento eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al eliminar evento", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView nombre , descripcion, frecuencia, fecha_inicio, fecha_final;
        Button btnEdit, btnDelete;

    public EventoViewHolder (@NonNull View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.textNombre);
        descripcion = itemView.findViewById(R.id.textDescripcion);
        frecuencia = itemView.findViewById(R.id.textFrecuencia);
        fecha_inicio = itemView.findViewById(R.id.textFechaInicio);
        fecha_final = itemView.findViewById(R.id.textFechaFinal);
        btnEdit = itemView.findViewById(R.id.btnEdit);
        btnDelete = itemView.findViewById(R.id.btnDelete);

}

    }
}