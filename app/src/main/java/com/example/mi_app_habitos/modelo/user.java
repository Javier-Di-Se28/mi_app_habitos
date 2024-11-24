package com.example.mi_app_habitos.modelo;

public class user {
    private String name;
    private String email;

    public user() { } // Constructor vacío para Firestore

    public user(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}