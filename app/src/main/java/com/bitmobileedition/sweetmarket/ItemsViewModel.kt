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


//package com.bitmobileedition.sweetmarket.ui.items
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.bitmobileedition.sweetmarket.Item
//import com.bitmobileedition.sweetmarket.data.model.Item
//import com.bitmobileedition.sweetmarket.data.repository.ItemsRepository
//import com.bitmobileedition.sweetmarket.util.Result
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch


//class ItemsViewModel(
//    private val itemsRepository: ItemsRepository
//) : ViewModel() {
//    private val _itemsState = MutableStateFlow<ItemsState>(ItemsState.Idle)
//    val itemsState: StateFlow<ItemsState> = _itemsState
//
//    private var originalItems: List<Item> = emptyList()
//
//    init {
//        loadItems()
//    }
//
//    fun loadItems() {
//        _itemsState.value = ItemsState.Loading
//        viewModelScope.launch {
//            when (val result = itemsRepository.getItems()) {
//                is Result.Success -> {
//                    originalItems = result.data
//                    _itemsState.value = ItemsState.Success(originalItems)
//                }
//                is Result.Failure -> {
//                    _itemsState.value = ItemsState.Error(result.exception.message ?: "Ошибка загрузки")
//                }
//            }
//        }
//    }
//
//    fun filter(query: String?) {
//        if (query.isNullOrEmpty()) {
//            _itemsState.value = ItemsState.Success(originalItems)
//        } else {
//            val filtered = originalItems.filter {
//                it.title.contains(query, ignoreCase = true) ||
//                        it.desc?.contains(query, ignoreCase = true) == true
//            }
//            _itemsState.value = ItemsState.Success(filtered)
//        }
//    }
//}
//
//sealed class ItemsState {
//    object Idle : ItemsState()
//    object Loading : ItemsState()
//    data class Success(val items: List<Item>) : ItemsState()
//    data class Error(val message: String) : ItemsState()
//}



//class ItemsViewModel(
//    private val repository: ItemsRepository
//) : ViewModel() {
//    private val _itemsState = MutableStateFlow<Resource<List<Item>>>(Resource.Loading())
//    val itemsState: StateFlow<Resource<List<Item>>> = _itemsState
//
//    private val _paginationState = MutableStateFlow(PaginationState())
//    val paginationState: StateFlow<PaginationState> = _paginationState
//
//    init {
//        loadItems()
//    }
//
//    fun loadItems(page: Int = 1, limit: Int = 20) {
//        viewModelScope.launch {
//            _itemsState.value = Resource.Loading()
//
//            when (val result = repository.getItems(page, limit)) {
//                is Result.Success -> {
//                    _itemsState.value = Resource.Success(result.data)
//                    _paginationState.value = PaginationState(
//                        currentPage = page,
//                        totalPages = calculateTotalPages(result.data.size, limit),
//                        canLoadMore = result.data.size >= limit
//                    )
//                }
//                is Result.Failure -> {
//                    _itemsState.value = Resource.Error(result.exception.message ?: "Unknown error")
//                }
//            }
//        }
//    }
//
//    fun loadNextPage() {
//        val currentState = _paginationState.value
//        if (currentState.canLoadMore && !currentState.isLoading) {
//            _paginationState.value = currentState.copy(isLoading = true)
//            loadItems(currentState.currentPage + 1)
//        }
//    }
//
//    private fun calculateTotalPages(itemCount: Int, limit: Int): Int {
//        return (itemCount + limit - 1) / limit
//    }
//}
//
//data class PaginationState(
//    val currentPage: Int = 1,
//    val totalPages: Int = 1,
//    val canLoadMore: Boolean = true,
//    val isLoading: Boolean = false
//)
//
//sealed class Resource<T>(val data: T? = null, val message: String? = null) {
//    class Loading<T> : Resource<T>()
//    class Success<T>(data: T) : Resource<T>(data = data)
//    class Error<T>(message: String, data: T? = null) : Resource<T>(data = data, message = message)
//}
