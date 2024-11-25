package com.example.mi_app_habitos.modelo;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Evento {

        private int id;
        private String nombre;
        private String descripcion;
        private String frecuencia;
        private String fecha_inicio;
        private String fecha_final;


    // Agregar los constructores, getters y setters
    public Evento() {
        }

    public Evento(int id, String nombre, String descripcion, String frecuencia, String fecha_inicio, String fecha_final) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.frecuencia = frecuencia;
        this.fecha_inicio = fecha_inicio;
        this.fecha_final = fecha_final;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(String fecha_final) {
        this.fecha_final = fecha_final;
    }
}
