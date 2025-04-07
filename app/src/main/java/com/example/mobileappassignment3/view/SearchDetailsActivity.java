package com.example.mobileappassignment3.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mobileappassignment3.R;
import com.example.mobileappassignment3.databinding.ActivitySearchDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SearchDetailsActivity extends AppCompatActivity {

    private ActivitySearchDetailBinding binding;
    private String currentPosterUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get movie imdbID from movie List
        String imdbID = getIntent().getStringExtra("imdbID");

        if(imdbID != null){
            loadMovieDetails(imdbID);
        }

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.buttonAddToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFavorites(imdbID);
            }
        });
    }

    private void addToFavorites(String imdbID) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        String uid = user.getUid();

        Map<String, Object> movieData = new HashMap<>();
        movieData.put("title", binding.textTitle.getText().toString());
        movieData.put("year", binding.textYear.getText().toString().replace("Year: ", ""));
        movieData.put("posterUrl", currentPosterUrl);
        movieData.put("description", binding.textPlot.getText().toString().replace("Plot: ", ""));

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("favorites")
                .document(imdbID)
                .set(movieData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void loadMovieDetails(String imdbID){
        JSONObject jsonObj = null;

        try {
            String apiUrl = "https://www.omdbapi.com/?apikey=" + getString(R.string.API_KEY) +
                    "&i=" + imdbID;
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            // close BufferedReader and connection
            reader.close();
            conn.disconnect();

            jsonObj = new JSONObject(response.toString());

            // load movie details
            currentPosterUrl = jsonObj.getString("Poster");
            Glide.with(this).load(currentPosterUrl).into(binding.imagePoster);

            binding.textTitle.setText(jsonObj.getString("Title"));
            binding.textYear.setText("Year: "+ jsonObj.getString("Year"));
            binding.textRated.setText("Rated: " + jsonObj.getString("Rated"));
            binding.textReleased.setText("Released: "+ jsonObj.getString("Released"));
            binding.textRuntime.setText("Runtime: "+ jsonObj.getString("Runtime"));
            binding.textGenre.setText("Genre: "+ jsonObj.getString("Genre"));
            binding.textDirector.setText("Director: "+ jsonObj.getString("Director"));
            binding.textWriter.setText("Writer: "+ jsonObj.getString("Writer"));
            binding.textActors.setText("Actors: " + jsonObj.getString("Actors"));
            binding.textPlot.setText("Plot: " + jsonObj.getString("Plot"));
            binding.textLanguage.setText("Language: " + jsonObj.getString("Language"));
            binding.textCountry.setText("Country: " + jsonObj.getString("Country"));
            binding.textAwards.setText("Awards: " + jsonObj.getString("Awards"));
            binding.textBoxOffice.setText("BoxOffice: " + jsonObj.getString("BoxOffice"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
