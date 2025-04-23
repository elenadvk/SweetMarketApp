package com.bitmobileedition.sweetmarket


import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ItemsViewModel(application: Application) : AndroidViewModel(application) {

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>> get() = _items

    private var allItems: List<Item> = emptyList()

    fun loadItems() {
        val sp = getApplication<Application>().getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val jsonList = sp.getString("items_list", null)

        val localItems: List<Item> = try {
            if (jsonList != null) {
                Json.decodeFromString(jsonList)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }

        allItems = localItems.reversed() // чтобы последние товары были сверху
        _items.value = allItems
    }

    fun filter(query: String = "") {
        if (query.isBlank()) {
            _items.value = allItems
        } else {
            _items.value = allItems.filter { it.title.contains(query, ignoreCase = true) }
        }
    }
}


//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.launch
//import kotlinx.serialization.decodeFromString
//import kotlinx.serialization.json.Json
//import java.io.BufferedReader
//import java.io.InputStreamReader
//
//class ItemsViewModel(application: Application) : AndroidViewModel(application) {
//    var originalList: List<Item>? = null
//    val items: MutableLiveData<List<Item>> = MutableLiveData()
//
//    init {
//        viewModelScope.launch {
//            val json = loadJsonFromAssets("app.json")
//            originalList = parseItemsJson(json)
//            items.postValue(originalList)
//        }
//    }
//
//    fun filter(query: String? = null) {
//        if (query != null)
//            items.value = originalList?.filter {
//                it.title.contains(query.trim(), ignoreCase = true) ||
//                        it.desc.contains(query.trim(), ignoreCase = true)
//            }
//    }
//
//    private fun loadJsonFromAssets(filename: String): String {
//        val assetManager = getApplication<Application>().assets
//        val inputStream = assetManager.open(filename)
//        val reader = BufferedReader(InputStreamReader(inputStream))
//        val stringBuilder = StringBuilder()
//        var line: String?
//        while (reader.readLine().also { line = it } != null) {
//            stringBuilder.append(line)
//        }
//        reader.close()
//        return stringBuilder.toString()
//    }
//
//    private fun parseItemsJson(json: String): List<Item> {
//        return try {
//            Json.decodeFromString(json)
//        } catch (e: Exception) {
//            emptyList()
//        }
//    }
//}
