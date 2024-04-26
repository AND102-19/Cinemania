package com.and102.cinemania

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Title(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("title")
    val title: String? = null,

) : Parcelable