package com.and102.cinemania

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import okhttp3.Headers

import com.and102.cinemania.History
private const val TMDB_API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"
class HistoryFragment : Fragment(R.layout.fragment_history) {
    var onListFragmentInteraction: ((Movie) -> Unit)? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvCinemaList)
        val progressBar = view.findViewById<ContentLoadingProgressBar>(R.id.progressBar)

        loadMovieHistory(recyclerView, progressBar)
    }

    private fun loadMovieHistory(recyclerView: RecyclerView, progressBar: ContentLoadingProgressBar) {
        val movieIds = History.clickedMovieIds
        Log.d("HistoryFragment", "Loading history, current movie IDs: $movieIds")


        if (movieIds.isEmpty()) {
            progressBar.hide()
        } else {
            fetchMovieDetails(movieIds, recyclerView, progressBar)
        }
    }

    private fun fetchMovieDetails(movieIds: List<Int>, recyclerView: RecyclerView, progressBar: ContentLoadingProgressBar) {
        val client = AsyncHttpClient()
        val detailedMovies = mutableListOf<Movie>()
        var fetchCount = 0
        Log.d("HistoryFragment", "movie size: ${movieIds.size}")
        movieIds.forEach { tmdbId ->
            val url = "https://api.themoviedb.org/3/movie/$tmdbId?api_key=$TMDB_API_KEY"
            client.get(url, object : JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                    Log.d("HistoryFragment", "JSON received: ${json.jsonObject}")
                    val movie = Gson().fromJson(json.jsonObject.toString(), Movie::class.java)
                    detailedMovies.add(movie)
                    fetchCount++
                    if (fetchCount == movieIds.size) {
                        recyclerView.adapter = MoviesRecyclerViewAdapter(detailedMovies, onListFragmentInteraction)
                        progressBar.hide()
                    }
                }

                override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String?, throwable: Throwable?) {
                    fetchCount++
                    if (fetchCount == movieIds.size) {
                        recyclerView.adapter = MoviesRecyclerViewAdapter(detailedMovies, onListFragmentInteraction)
                        progressBar.hide()
                        Log.e("HistoryFragment", "Failed to fetch movie details: $errorResponse")
                    }
                }
            })
        }
    }




}
