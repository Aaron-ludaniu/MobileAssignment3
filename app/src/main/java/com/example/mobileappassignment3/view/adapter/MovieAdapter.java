package com.example.mobileappassignment3.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mobileappassignment3.R;
import com.example.mobileappassignment3.model.MovieModel;
import com.example.mobileappassignment3.view.SearchDetailsActivity;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder>{

    public interface OnMovieClickListener {
        void onMovieClick(MovieModel movie);
    }

    private Context context;
    private List<MovieModel> movieList;
    private OnMovieClickListener listener;


    public MovieAdapter(Context context, List<MovieModel> movieList, OnMovieClickListener listener) {
        this.context = context;
        this.movieList = movieList;
        this.listener = listener;
    }

    public void updateData(List<MovieModel> movies) {
        this.movieList = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieModel movie = movieList.get(position);
        holder.textTitle.setText(movie.getTitle());
        holder.textYear.setText("Year: " + movie.getYear());
        Glide.with(context).load(movie.getPoster()).into(holder.imagePoster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onMovieClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
