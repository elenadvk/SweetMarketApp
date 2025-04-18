package com.bitmobileedition.sweetmarket

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader

// Изменён ViewModel на AndroidViewModel, так как нужен доступ к Context
class ItemsViewModel(application: Application) : AndroidViewModel(application) {
    var originalList: List<Item>? = null
    val items: MutableLiveData<List<Item>> = MutableLiveData()

    init {
        viewModelScope.launch {
            // Чтение JSON из локального файла в assets
            val json = loadJsonFromAssets("app.json")
            originalList = parseItemsJson(json)
            items.postValue(originalList)
        }
    }

    // Функция для фильтрации элементов
    fun filter(query: String? = null) {
        if (query != null)
            items.value = originalList?.filter {
                it.title.contains(query.trim(), ignoreCase = true) ||
                        it.desc.contains(query.trim(), ignoreCase = true)
            }
    }

    // Чтение JSON из локального файла assets
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

    // Парсинг JSON строки в список объектов Item
    private fun parseItemsJson(json: String): List<Item> {
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Загрузка изображения из папки assets
    fun loadImageFromAssets(imageName: String): Bitmap? {
        return try {
            val inputStream = getApplication<Application>().assets.open(imageName)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }
}




//package com.bitmobileedition.sweetmarket
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
//import kotlinx.coroutines.launch
//import kotlinx.serialization.ExperimentalSerializationApi
//import kotlinx.serialization.json.Json
//import okhttp3.MediaType
//import retrofit2.Retrofit
//
//class ItemsViewModel: ViewModel() {
//    @OptIn(ExperimentalSerializationApi::class)
//    val retrofit = Retrofit.Builder()
//        .baseUrl("http://84.246.85.148:8080") //---------------------------------------------------------------------------------
//        .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
//        .build().create(ApiService::class.java)
//    var originalList: List<Item>? = null
//    val items: MutableLiveData<List<Item>> = MutableLiveData()
//    init {
//        viewModelScope.launch {
//            originalList = retrofit.getItems()
//            items.postValue(originalList)
//        }
//    }
//    fun filter(query: String? = null){
//        if(query != null)
//            items.value = originalList?.filter { it.title.contains(query.trim(), ignoreCase = true) || it.desc.contains(query.trim(), ignoreCase = true) }
//    }
//}