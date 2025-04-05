package com.example.mobileappassignment3.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobileappassignment3.R;
import com.example.mobileappassignment3.model.MovieModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieViewModel extends ViewModel {
    private MutableLiveData<List<MovieModel>> movieList = new MutableLiveData<>();

    public LiveData<List<MovieModel>> getMovies() {
        return movieList;
    }

    public void searchMovies(String searchWord, String apiKey){
        try {
            String apiUrl = "https://www.omdbapi.com/?apikey=" + apiKey +
                    "&s=" + searchWord + "&type=movie";
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

            JSONObject jsonObj = new JSONObject(response.toString());
            if (jsonObj.has("Search")) {
                JSONArray jsonArray = jsonObj.getJSONArray("Search");

                List<MovieModel> movies = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    MovieModel movie = new MovieModel();
                    movie.setTitle(obj.getString("Title"));
                    movie.setYear(obj.getString("Year"));
                    movie.setImdbID(obj.getString("imdbID"));
                    movie.setType(obj.getString("Type"));
                    movie.setPoster(obj.getString("Poster"));

                    movies.add(movie);
                }
                movieList.postValue(movies);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
