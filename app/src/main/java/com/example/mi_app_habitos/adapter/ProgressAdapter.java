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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder> {


        private List<Evento> eventos;

        public ProgressAdapter(List<Evento> eventos) {
            this.eventos = eventos;
        }

        @NonNull
        @Override
        public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Asegúrate de inflar el layout correcto para cada item (no activity_estadisticas)
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estadisticas, parent, false);
            return new ProgressViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {
            Evento evento = eventos.get(position);

            holder.textNombreEvento.setText(evento.getNombre());
            int progreso = calcularProgreso(evento.getFecha_inicio(), evento.getFecha_final());
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

        // Método para calcular el progreso basado en fechas
        private int calcularProgreso(String fechaInicio, String fechaFin) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                Date inicio = sdf.parse(fechaInicio);
                Date fin = sdf.parse(fechaFin);
                Date hoy = new Date();

                if (hoy.before(inicio)) {
                    return 0;
                } else if (hoy.after(fin)) {
                    return 100;
                } else {
                    long duracionTotal = fin.getTime() - inicio.getTime();
                    long duracionTranscurrida = hoy.getTime() - inicio.getTime();
                    return (int) ((duracionTranscurrida * 100) / duracionTotal);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }