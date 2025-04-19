package com.bitmobileedition.sweetmarket

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader

class ItemsViewModel(application: Application) : AndroidViewModel(application) {
    var originalList: List<Item>? = null
    val items: MutableLiveData<List<Item>> = MutableLiveData()

    init {
        viewModelScope.launch {
            val json = loadJsonFromAssets("app.json")
            originalList = parseItemsJson(json)
            items.postValue(originalList)
        }
    }

    fun filter(query: String? = null) {
        if (query != null)
            items.value = originalList?.filter {
                it.title.contains(query.trim(), ignoreCase = true) ||
                        it.desc.contains(query.trim(), ignoreCase = true)
            }
    }

    private fun loadJsonFromAssets(filename: String): String {
        val assetManager = getApplication<Application>().assets
        val inputStream = assetManager.open(filename)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
        reader.close()
        return stringBuilder.toString()
    }

    private fun parseItemsJson(json: String): List<Item> {
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
