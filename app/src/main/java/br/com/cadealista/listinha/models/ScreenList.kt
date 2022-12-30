package br.com.cadealista.listinha.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Entity(tableName = "screen_list_table")
@Parcelize
data class ScreenList(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @Expose
    val name: String = "",
    @Expose
    val description: String = "",
    val color: String = ""
): Parcelable
