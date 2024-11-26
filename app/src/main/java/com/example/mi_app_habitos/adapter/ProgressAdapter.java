package com.example.mi_app_habitos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mi_app_habitos.R;
import com.example.mi_app_habitos.modelo.Evento;

import java.util.Date;
import java.util.List;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder> {

    private List<Evento> eventos;

    public ProgressAdapter(List<Evento> eventos) {
        this.eventos = eventos;
    }

    @NonNull
    @Override
    public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Asegúrate de inflar el layout correcto para cada item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estadisticas, parent, false);
        return new ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {
        Evento evento = eventos.get(position);

        holder.textNombreEvento.setText(evento.getNombre());

        // Usamos los métodos que implementaste en el modelo para obtener las fechas como Date
        Date inicio = evento.getFechaInicioAsDate();
        Date fin = evento.getFechaFinalAsDate();

        int progreso = calcularProgreso(inicio, fin);

        holder.progressBar.setProgress(progreso);
        holder.progressText.setText(progreso + "%");
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {
        TextView textNombreEvento, progressText;
        ProgressBar progressBar;

        ProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            textNombreEvento = itemView.findViewById(R.id.textNombreEvento);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressText = itemView.findViewById(R.id.progressText);
        }
    }

    // Método para calcular el progreso basado en las fechas (Date)
    private int calcularProgreso(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return 0;
        }

        Date hoy = new Date();

        if (hoy.before(fechaInicio)) {
            return 0;
        } else if (hoy.after(fechaFin)) {
            return 100;
        } else {
            long duracionTotal = fechaFin.getTime() - fechaInicio.getTime();
            long duracionTranscurrida = hoy.getTime() - fechaInicio.getTime();
            return (int) ((duracionTranscurrida * 100) / duracionTotal);
        }
    }
}
