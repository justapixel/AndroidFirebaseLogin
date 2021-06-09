package com.example.androidfirebaselogin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Service (
    var id: String = "",
    val type: String = "",
    val description: String = "",
    val date: String = "",
    val userId: String = "",
    val status: Int = 0,
 ) : Parcelable