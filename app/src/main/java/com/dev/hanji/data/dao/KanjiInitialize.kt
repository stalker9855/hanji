package com.dev.hanji.data.dao

import android.content.Context
import com.dev.hanji.data.database.AppDatabase
import com.dev.hanji.data.model.KanjiEntity
import com.dev.hanji.data.desirializers.KanjiEntityDeserializer
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

fun getJsonFromAssets(context: Context, fileName: String): String? {
    return try {
        context.assets.open(fileName).bufferedReader().use {it.readText()}
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun insertKanjiFromJson(context: Context) {
    val jsonString = getJsonFromAssets(context = context, "kanji.json")
    jsonString?.let {
        val gson = GsonBuilder()
            .registerTypeAdapter(KanjiEntity::class.java, KanjiEntityDeserializer())
            .create()

        val listType = object : TypeToken<List<KanjiEntity>>() {}.type
        val kanji: List<KanjiEntity>  = gson.fromJson(it, listType)

        val db = AppDatabase.getInstance(context)
        val kanjiDao = db.kanjiDao

        runBlocking(Dispatchers.IO) {
            kanjiDao.insert(kanji)
        }

    }
}
