package com.and102.cinemania

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers

private const val WATCHMODE_API_KEY = "a0JMajQRghyDuTslLkf6BSfovXBW7yWTcb2iw8wS"
private const val TMDB_API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"
private const val NETFLIX_SOURCE_ID = "203"

/*
 * The class for the movies fragment in the app.
 */
class HomePageFragment : Fragment() {
    var onListFragmentInteractionListener: ((Movie) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        val progressBar = view.findViewById<ContentLoadingProgressBar>(R.id.progressBar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvCinemaList)
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        updateAdapter(progressBar, recyclerView)
        return view
    }
    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()
        val client = AsyncHttpClient()
        val tmdbIds = mutableListOf<Int>()

        // Fetch the list of TMDB IDs from Watchmode
        client.get("https://api.watchmode.com/v1/list-titles/?source_ids=$NETFLIX_SOURCE_ID&apiKey=$WATCHMODE_API_KEY", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                val moviesArray = json.jsonObject.getJSONArray("titles")
                for (i in 0 until moviesArray.length()) {
                    val tmdbId = moviesArray.getJSONObject(i).optInt("tmdb_id")
                    tmdbIds.add(tmdbId)
                }

                // Now fetch details from TMDB for each movie ID
                val detailedMovies = mutableListOf<Movie>()
                var fetchCount = 0
                tmdbIds.forEach { tmdbId ->
                    Log.d("HomePageFragment", "Fetching details for TMDB ID: $tmdbId")
                    client.get("https://api.themoviedb.org/3/movie/$tmdbId?api_key=$TMDB_API_KEY", object : JsonHttpResponseHandler() {
                        override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                            val movie = Gson().fromJson(json.jsonObject.toString(), Movie::class.java)
                            detailedMovies.add(movie)
                            fetchCount++
                            // Update RecyclerView once all fetches are complete
                            if (fetchCount == tmdbIds.size) {
                                recyclerView.adapter = MoviesRecyclerViewAdapter(detailedMovies, onListFragmentInteractionListener)
                                progressBar.hide()
                            }
                        }

                        override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                            fetchCount++
                            Log.e("HomePageFragment", "Failed to fetch movie details: $errorResponse")
                            // Update RecyclerView even if some fetches fail
                            if (fetchCount == tmdbIds.size) {
                                recyclerView.adapter = MoviesRecyclerViewAdapter(detailedMovies, onListFragmentInteractionListener)
                                progressBar.hide()
                            }
                        }
                    })
                }
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String, throwable: Throwable?) {
                progressBar.hide()
                Log.e("HomePageFragment", "Failed to fetch TMDB IDs: $errorResponse")
            }
        })
    }

}