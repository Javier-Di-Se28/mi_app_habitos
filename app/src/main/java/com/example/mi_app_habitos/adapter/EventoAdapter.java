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

import com.example.mi_app_habitos.R;
import com.example.mi_app_habitos.modelo.Evento;

import java.util.List;

public class EventoAdapter  extends RecyclerView.Adapter<EventoAdapter.EventoViewHolder> {
    private List<Evento> eventos;
    private Context context;

    public EventoAdapter(List<Evento> eventos, Context context) {
        this.eventos = eventos;
        this.context = context;
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
        holder.fecha_inicio.setText(evento.getFecha_inicio().toString());
        holder.fecha_final.setText(evento.getFecha_final().toString());
        // Acciones de botones (Editar/Eliminar)
        holder.btnEdit.setText("Editar");
        holder.btnDelete.setText("Eliminar");

        // Acciones de botones (Editar/Eliminar)
        holder.btnEdit.setOnClickListener(v -> {
            Toast.makeText(context, "Editar " + evento.getNombre(), Toast.LENGTH_SHORT).show();
        });

        holder.btnDelete.setOnClickListener(v -> {
            Toast.makeText(context, "Eliminar " + evento.getNombre(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    static class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView nombre , descripcion, fecha_inicio, fecha_final;
        Button btnEdit, btnDelete;

    public EventoViewHolder (@NonNull View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.textNombre);
        descripcion = itemView.findViewById(R.id.textDescripcion);
        fecha_inicio = itemView.findViewById(R.id.textFechaInicio);
        fecha_final = itemView.findViewById(R.id.textFechaFinal);
        btnEdit = itemView.findViewById(R.id.btnEdit);
        btnDelete = itemView.findViewById(R.id.btnDelete);

}

    }
}