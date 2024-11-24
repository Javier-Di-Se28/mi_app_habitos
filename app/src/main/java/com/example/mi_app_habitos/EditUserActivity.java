package com.example.mi_app_habitos;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class EditUserActivity extends AppCompatActivity {
    FirebaseFirestore mFirestore;
    EditText editName, editEmail;
    Button saveButton;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // Obtener el ID del usuario
        userId = getIntent().getStringExtra("userId");

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "ID de usuario no válido", Toast.LENGTH_SHORT).show();
            Log.d("EditUserActivity", "userId recibido: " + userId);
            finish(); // Salir de la actividad si no hay un ID válido
            return;
        }

        // Configurar la vista
        mFirestore = FirebaseFirestore.getInstance();
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        saveButton = findViewById(R.id.saveButton);

        // Cargar datos actuales
        mFirestore.collection("user").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        editName.setText(documentSnapshot.getString("name"));
                        editEmail.setText(documentSnapshot.getString("email"));
                    } else {
                        Toast.makeText(this, "No se encontró el usuario", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al cargar datos del usuario", Toast.LENGTH_SHORT).show()
                );

        // Guardar cambios
        saveButton.setOnClickListener(v -> {
            String newName = editName.getText().toString().trim();
            String newEmail = editEmail.getText().toString().trim();

            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(EditUserActivity.this, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show();
                return;
            }

            mFirestore.collection("user").document(userId).update("name", newName, "email", newEmail)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EditUserActivity.this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK); // Notificar éxito a UserProfileActivity
                        finish(); // Cerrar la actividad
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(EditUserActivity.this, "Error al actualizar usuario", Toast.LENGTH_SHORT).show()
                    );
        });
    }

    }
