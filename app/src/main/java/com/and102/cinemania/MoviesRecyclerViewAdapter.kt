package com.and102.cinemania

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.and102.cinemania.History


/**
 * [RecyclerView.Adapter] that can display a [Movie] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MoviesRecyclerViewAdapter(
    private val movies: List<Movie>,
    private val mListener: ((Movie) -> Unit)?
) : RecyclerView.Adapter<MoviesRecyclerViewAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    inner class MovieViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: Movie? = null
        val mMovieTitle: TextView = mView.findViewById<View>(R.id.textViewMovieTitle) as TextView
        val mMovieDescription: TextView = mView.findViewById<View>(R.id.textViewMovieDescription) as TextView
        val mMoviePoster: ImageView = mView.findViewById<View>(R.id.imageViewMoviePoster) as ImageView

        override fun toString(): String {
            return mMovieTitle.toString() + " '" + mMovieDescription.text + "'"
        }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.mItem = movie
        holder.mMovieTitle.text = movie.title
        holder.mMovieDescription.text = movie.overview
        holder.mView.setOnClickListener {
            holder.mItem?.let { movie ->
                Log.d("MoviesAdapter", "Movie clicked: ${movie.id}")
                History.clickedMovieIds.add(movie.id)
                Log.d("HistoryFragment", "Loading history, current movie IDs: ${History.clickedMovieIds}")
                val context = holder.mView.context
                val intent = Intent(context, MovieDetailActivity::class.java)
                intent.putExtra("movie", movie)
                context.startActivity(intent)
                mListener?.invoke(movie)
            }
        }


        Glide.with(holder.mView)
            .load(movie.getPosterImageUrl())
            .centerInside()
            .into(holder.mMoviePoster)
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}