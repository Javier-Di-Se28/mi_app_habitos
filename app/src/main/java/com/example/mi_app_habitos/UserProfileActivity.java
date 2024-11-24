package com.example.mi_app_habitos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class UserProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView emailText, usernameText;
    private Button editButton, changePhotoButton;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileImage = findViewById(R.id.profileImage);
        emailText = findViewById(R.id.emailText);
        usernameText = findViewById(R.id.usernameText);
        editButton = findViewById(R.id.editButton);
        changePhotoButton = findViewById(R.id.changePhotoButton);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            loadUserData(user.getUid());
        }

        editButton.setOnClickListener(v -> {
            if (user != null) {
                String userId = user.getUid();  // Obtén el userId del usuario autenticado

                Intent intent = new Intent(UserProfileActivity.this, EditUserActivity.class);
                intent.putExtra("userId", userId);  // Pasa el userId al intent
                startActivity(intent);
            } else {
                Toast.makeText(UserProfileActivity.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            }

        });
        changePhotoButton.setOnClickListener(v -> {
            // Crear un Intent para abrir la galería de imágenes
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 101);  // 101 es el código de solicitud que usaremos más tarde
        });
    }



    private void updateProfilePhotoUrl(String photoUrl) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("user").document(user.getUid())
                    .update("photoUrl", photoUrl)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(UserProfileActivity.this, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(UserProfileActivity.this, "Error al actualizar foto", Toast.LENGTH_SHORT).show();
                    });
        }
    }



    private void loadUserData(String userId) {
        firestore.collection("user").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");
                        String photoUrl = documentSnapshot.getString("photoUrl");

                        usernameText.setText(name != null ? name : "Sin nombre");
                        emailText.setText(email != null ? email : "Sin correo");


                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(UserProfileActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
                );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            FirebaseUser user = auth.getCurrentUser();
            if (user != null) {
                loadUserData(user.getUid());

            }
            Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show();
        }
    }

}
