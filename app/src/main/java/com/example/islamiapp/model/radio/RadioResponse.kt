package com.example.islamiapp.model.radio

import com.google.gson.annotations.SerializedName

data class RadioResponse(
    @SerializedName("radios") val radios: List<RadioDM>
)