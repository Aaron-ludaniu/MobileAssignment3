package com.example.mobileappassignment3.view;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobileappassignment3.R;
import com.example.mobileappassignment3.databinding.ActivityFavoritesBinding;
import com.example.mobileappassignment3.model.MovieModel;
import com.example.mobileappassignment3.view.adapter.MovieAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    private ActivityFavoritesBinding binding;
    private MovieAdapter adapter;
    private List<MovieModel> movieList = new ArrayList<>();

    private FirebaseFirestore db;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MovieAdapter(this, movieList, movie -> {
            // click to gonna favorites detail page
            Intent intent = new Intent(this, FavoritesDetailsActivity.class);
            intent.putExtra("imdbID", movie.getImdbID());
            startActivity(intent);
        });
        binding.recyclerViewFavorites.setAdapter(adapter);

        binding.buttonGoToSearch.setOnClickListener(v ->  {
            startActivity(new Intent(this, SearchActivity.class));
            finish();
        });

        db = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadFavorites();

    }

    private void loadFavorites() {
        db.collection("users")
                .document(uid)
                .collection("favorites")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    movieList.clear();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        MovieModel movie = new MovieModel();
                        movie.setImdbID(doc.getId());
                        movie.setTitle(doc.getString("title"));
                        movie.setYear(doc.getString("year"));
                        movie.setPoster(doc.getString("posterUrl"));
                        movie.setDescription(doc.getString("description"));
                        movieList.add(movie);
                    }
                    adapter.updateData(movieList);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load favorites", Toast.LENGTH_SHORT).show();
                });
    }
}