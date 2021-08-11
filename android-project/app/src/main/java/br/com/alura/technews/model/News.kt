package br.com.alura.technews.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class News(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @SerializedName("titulo")
    val title: String = "",
    @SerializedName("texto")
    val text: String = ""
)
