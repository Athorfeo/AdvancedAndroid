package com.obcompany.advancedandroid.app.model

import com.google.gson.annotations.SerializedName

/**
 *  @author Juan D. Ortiz - @Athorfeo (Github)
 *  @Date 12/06/2019
 */
data class Post(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String
)