package com.example.mi_app_habitos.modelo;

import java.util.Date;

public class Evento {

        private int id;
        private String nombre;
        private String descripcion;
        private Date fecha_inicio;
        private Date fecha_final;

        // Agregar los constructores, getters y setters
    public Evento() {
        }

    public Evento(int id, String nombre, String descripcion, Date fecha_inicio, Date fecha_final) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha_inicio = fecha_inicio;
        this.fecha_final = fecha_final;
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

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(Date fecha_final) {
        this.fecha_final = fecha_final;
    }
}
