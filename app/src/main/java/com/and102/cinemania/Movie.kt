package com.and102.cinemania

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("overview")
    val overview: String? = null,

    @SerializedName("poster_path")
    val posterPath: String? = null,

    @SerializedName("release_date")
    val releaseDate: String? = null,

    @SerializedName("vote_average")
    val rating: Double = 0.0,

    @SerializedName("video_key")
    val videoKey: String? = null,

    @SerializedName("review_text")
    val reviewText: String? = null

) : Parcelable {
    fun getPosterImageUrl(): String {
        val baseUrl = "https://image.tmdb.org/t/p/w500"
        return baseUrl + (posterPath ?: "")
    }
}
@Parcelize
data class Genre(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
) : Parcelable