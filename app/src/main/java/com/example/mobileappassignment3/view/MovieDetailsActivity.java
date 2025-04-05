package com.example.mobileappassignment3.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mobileappassignment3.R;
import com.example.mobileappassignment3.databinding.ActivityMovieDetailBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MovieDetailsActivity extends AppCompatActivity {

    private ActivityMovieDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMovieDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Get movie imdbID from movie List
        String imdbID = getIntent().getStringExtra("imdbID");

        if(imdbID != null){
            loadMovieDetails(imdbID);
        }
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
            Glide.with(this).load(jsonObj.getString("Poster")).into(binding.imagePoster);

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
