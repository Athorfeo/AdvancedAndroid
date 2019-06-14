package com.obcompany.advancedandroid.app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tableId")
    val tableId: Long,

    @SerializedName("id") val id: Int,

    @ColumnInfo(name = "name")
    @SerializedName("name") val name: String,

    @ColumnInfo(name = "email")
    @SerializedName("email") val email: String,

    @ColumnInfo(name = "phone")
    @SerializedName("phone") val phone: String
): Serializable