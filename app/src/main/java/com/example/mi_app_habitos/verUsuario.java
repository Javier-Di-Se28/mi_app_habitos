package com.example.mi_app_habitos;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mi_app_habitos.adapter.UserAdapter;
import com.example.mi_app_habitos.modelo.user;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class verUsuario extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseFirestore mFirestore;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuario);

        recyclerView = findViewById(R.id.recyclerView);
        mFirestore = FirebaseFirestore.getInstance();

        Query query = mFirestore.collection("user");
        FirestoreRecyclerOptions<user> options = new FirestoreRecyclerOptions.Builder<user>()
                .setQuery(query, user.class)
                .build();

        adapter = new UserAdapter(options, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
