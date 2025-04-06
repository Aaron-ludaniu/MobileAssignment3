package com.example.mobileappassignment3.view;

import android.os.Bundle;
import android.os.StrictMode;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mobileappassignment3.databinding.ActivitySearchBinding;
import com.example.mobileappassignment3.view.adapter.MovieAdapter;
import com.example.mobileappassignment3.R;
import com.example.mobileappassignment3.model.MovieModel;
import com.example.mobileappassignment3.viewmodel.MovieViewModel;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private MovieAdapter movieAdapter;
    private ArrayList<MovieModel> movieList = new ArrayList<>();
    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this));

        movieAdapter = new MovieAdapter(this, movieList);
        binding.recyclerViewMovies.setAdapter(movieAdapter);

        // grant nerwork permisson
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        // Binding MovieViewModel
        MovieViewModel viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Listening to LiveData changes
        viewModel.getMovies().observe(this, movies -> {
            movieAdapter.updateData(movies);
        });

        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.searchMovies(binding.editTextSearch.getText().toString(),
                        getString(R.string.API_KEY));
            }
        });
    }
}