package com.example.mobileappassignment3.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.mobileappassignment3.R;
import com.example.mobileappassignment3.databinding.ActivityFavoritesBinding;
import com.example.mobileappassignment3.databinding.ActivityFavoritesDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FavoritesDetailsActivity extends AppCompatActivity {

    private ActivityFavoritesDetailsBinding binding;

    private FirebaseFirestore db;
    private String uid;
    private String imdbID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavoritesDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        imdbID = getIntent().getStringExtra("imdbID");

        loadFavoriteDetails();

        binding.buttonBack.setOnClickListener(v -> {
            finish();
        });

        binding.buttonUpdate.setOnClickListener(v -> {
            updateDescription();
        });

        binding.buttonDelete.setOnClickListener(v -> {
            deleteFavorite();
        });
    }

    private void loadFavoriteDetails() {
        db.collection("users")
                .document(uid)
                .collection("favorites")
                .document(imdbID)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        binding.textTitle.setText(doc.getString("title"));
                        binding.editTextDescription.setText(doc.getString("description"));
                        Glide.with(this).load(doc.getString("posterUrl")).into(binding.imagePoster);
                    } else {
                        Toast.makeText(this, "Favorite Movie detail not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void updateDescription() {
        String newDesc = binding.editTextDescription.getText().toString().trim();
        db.collection("users")
                .document(uid)
                .collection("favorites")
                .document(imdbID)
                .update("description", newDesc)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void deleteFavorite() {
        db.collection("users")
                .document(uid)
                .collection("favorites")
                .document(imdbID)
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, FavoritesActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}