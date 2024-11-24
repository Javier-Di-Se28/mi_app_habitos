package com.example.mi_app_habitos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mi_app_habitos.EditUserActivity;
import com.example.mi_app_habitos.R;
import com.example.mi_app_habitos.modelo.user;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class UserAdapter extends FirestoreRecyclerAdapter<user, UserAdapter.UserViewHolder> {

    private Context context;

    public UserAdapter(@NonNull FirestoreRecyclerOptions<user> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull user model) {
        holder.username.setText(model.getName());
        holder.useremail.setText(model.getEmail());

        // Configurar el botón para editar
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditUserActivity.class);
            intent.putExtra("userId", getSnapshots().getSnapshot(position).getId());
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView username, useremail;
        Button editButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            useremail = itemView.findViewById(R.id.useremail);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
