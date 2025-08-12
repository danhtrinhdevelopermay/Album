package com.ios18photos.app.ui.albums

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ios18photos.app.data.model.Album
import com.ios18photos.app.data.repository.MediaRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for AlbumsFragment
 */
class AlbumsViewModel(application: Application) : AndroidViewModel(application) {

    private val mediaRepository = MediaRepository(application)

    private val _myAlbums = MutableLiveData<List<Album>>()
    val myAlbums: LiveData<List<Album>> = _myAlbums

    private val _defaultAlbums = MutableLiveData<List<Album>>()
    val defaultAlbums: LiveData<List<Album>> = _defaultAlbums

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Load all albums from device storage
     */
    fun loadAlbums() {
        _isLoading.value = true
        
        viewModelScope.launch {
            try {
                val allAlbums = mediaRepository.loadAlbums()
                
                // Separate user albums from default albums
                val userAlbums = allAlbums.filter { !it.isDefault }
                val systemAlbums = allAlbums.filter { it.isDefault }
                
                _myAlbums.value = userAlbums
                _defaultAlbums.value = systemAlbums
                
            } catch (e: Exception) {
                // Handle error
                _myAlbums.value = emptyList()
                _defaultAlbums.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Create a new album
     * Note: In a real implementation, this would create the album in storage
     */
    fun createAlbum(name: String) {
        // For now, just reload albums
        // In a real implementation, you would:
        // 1. Create the album in storage/database
        // 2. Update the local list
        // 3. Notify the UI
        loadAlbums()
    }
}