package com.ios18photos.app.ui.album

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ios18photos.app.data.model.Album
import com.ios18photos.app.data.model.MediaItem
import com.ios18photos.app.data.repository.MediaRepository
import kotlinx.coroutines.launch

class AlbumDetailViewModel(context: Context) : ViewModel() {
    
    private val mediaRepository = MediaRepository(context)
    
    private val _photos = MutableLiveData<List<MediaItem>>()
    val photos: LiveData<List<MediaItem>> = _photos
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    fun loadAlbumPhotos(album: Album) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // Load photos for the specific album
                val albumPhotos = mediaRepository.getPhotosForAlbum(album.id)
                _photos.value = albumPhotos
                
            } catch (e: Exception) {
                _error.value = "Failed to load album photos: ${e.message}"
                _photos.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun refreshPhotos(album: Album) {
        loadAlbumPhotos(album)
    }
}

class AlbumDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlbumDetailViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}