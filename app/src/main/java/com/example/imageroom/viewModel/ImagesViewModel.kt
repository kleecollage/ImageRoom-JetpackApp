package com.example.imageroom.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.imageroom.model.ImageModel
import com.example.imageroom.room.AppDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

// usamos AndroidViewModel cuando nos queremos saltar la inyeccion de dependencias
class ImagesViewModel(application: Application): AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDataBase::class.java,
        "images_database"
    ).build()

    private val _imageList = MutableStateFlow<List<ImageModel>>(emptyList())
    val imageList = _imageList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            db.imageDao().getImages().collect { items ->
                _imageList.value = items
            }
        }
    }

    fun insertImage(item: ImageModel) {
        viewModelScope.launch(Dispatchers.IO) {
            db.imageDao().insertImage(item)
        }
    }

    fun deleteImage(item: ImageModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deletePhoto(item.ruta)
            db.imageDao().deleteImage(item)
        }
    }

    private fun deletePhoto(photoPath: String) {
        val file = File(photoPath)
        if (file.exists()) {
            file.delete()
        }
    }
}