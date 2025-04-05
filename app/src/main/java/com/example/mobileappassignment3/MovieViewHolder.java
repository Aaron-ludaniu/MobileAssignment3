package com.example.mobileappassignment3;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    TextView textTitle, textYear;
    ImageView imagePoster;

    public MovieViewHolder(View itemView) {
        super(itemView);
        textTitle = itemView.findViewById(R.id.textTitle);
        textYear = itemView.findViewById(R.id.textYear);

        imagePoster = itemView.findViewById(R.id.imagePoster);
    }
}
