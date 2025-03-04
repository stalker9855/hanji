package com.dev.hanji.kanji

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Entity(tableName = "kanji")
data class KanjiEntity (
    @PrimaryKey
    @ColumnInfo("character")
    val character: String,

    @ColumnInfo("strokes")
    val strokes: Int,

    @ColumnInfo("meanings")
    var meanings: List<String>,

    @ColumnInfo("readings_on")
    val readingsOn: List<String>,

    @ColumnInfo("readings_kun")
    val readingsKun: List<String>,

    @ColumnInfo("unicode_hex")
    val unicodeHex: String

)

class KanjiConverters {

     @TypeConverter
     fun convertListToString(value: List<String>): String = Gson().toJson(value)

    @TypeConverter
    fun convertStringToList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, type) ?: emptyList()
    }


}

