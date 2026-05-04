package com.example.islamiapp.model.radio

import com.google.gson.annotations.SerializedName


data class RadioDM(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)