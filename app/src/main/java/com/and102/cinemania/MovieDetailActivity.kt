package com.and102.cinemania

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class MovieDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val movie = intent.getParcelableExtra<Movie>("movie")

        val titleTextView = findViewById<TextView>(R.id.movieDetailTitle)
        val descriptionTextView = findViewById<TextView>(R.id.movieDetailDescription)
        val posterImageView = findViewById<ImageView>(R.id.movieDetailPoster)
        val releaseDateTextView = findViewById<TextView>(R.id.movieDetailReleaseDate)
        val ratingTextView = findViewById<TextView>(R.id.movieDetailRating)

        Glide.with(this)
            .load(movie?.getPosterImageUrl())
            .error(R.drawable.ic_launcher_background)
            .centerInside()
            .into(posterImageView)

        Log.d("MovieDetailActivity", "Movie: ${movie?.releaseDate}")
        Log.d("MovieDetailActivity", "Movie: ${movie?.reviewText}")
        titleTextView.text = movie?.title
        descriptionTextView.text = if (movie?.overview.isNullOrEmpty()) "Description not available" else movie?.overview
        releaseDateTextView.text = "Release Date\n" + movie?.releaseDate
        val roundedRating = String.format("%.2f", movie?.rating)
        ratingTextView.text = "Rating\n$roundedRating/10"
    }
}