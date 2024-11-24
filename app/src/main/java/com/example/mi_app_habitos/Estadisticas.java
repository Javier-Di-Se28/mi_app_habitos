package com.example.mi_app_habitos;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Estadisticas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);
        // Accede al ProgressBar y actualiza el progreso
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(60); // Cambia el progreso a 60%

    }
}