package com.example.mi_app_habitos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mi_app_habitos.ApiService;
import com.example.mi_app_habitos.R;
import com.example.mi_app_habitos.modelo.Evento;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventoAdapter  extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {
    private List<Evento> eventos;
    private Context context;
    private ApiService apiService;

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
        holder.frecuencia.setText(evento.getFrecuencia().toString());
        holder.fecha_inicio.setText(evento.getFecha_inicio().toString());
        holder.fecha_final.setText(evento.getFecha_final().toString());
        // Acciones de botones (Editar/Eliminar)
        holder.btnEdit.setText("Editar");
        holder.btnDelete.setText("Eliminar");

        // Acciones de botones (Editar/Eliminar)
        holder.btnEdit.setOnClickListener(v -> {
            Toast.makeText(context, "Editar " + evento.getNombre(), Toast.LENGTH_SHORT).show();
        });

        holder.btnDelete.setText("Eliminar");
        holder.btnDelete.setOnClickListener(v -> {
            eliminarEvento(position, evento.getId()); // Eliminar el evento
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