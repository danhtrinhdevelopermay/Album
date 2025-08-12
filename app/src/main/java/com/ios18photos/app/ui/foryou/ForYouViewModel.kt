package com.ios18photos.app.ui.foryou

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ios18photos.app.data.model.MediaItem
import com.ios18photos.app.data.repository.MediaRepository
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * ViewModel for ForYouFragment
 */
class ForYouViewModel(application: Application) : AndroidViewModel(application) {

    private val mediaRepository = MediaRepository(application)

    private val _favorites = MutableLiveData<List<MediaItem>>()
    val favorites: LiveData<List<MediaItem>> = _favorites

    private val _recent = MutableLiveData<List<MediaItem>>()
    val recent: LiveData<List<MediaItem>> = _recent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Load curated content for For You tab
     */
    fun loadCuratedContent() {
        _isLoading.value = true
        
        viewModelScope.launch {
            try {
                val allItems = mediaRepository.loadAllMediaItems()
                
                // Get favorite items
                val favoriteItems = allItems.filter { it.isFavorite }.take(10)
                _favorites.value = favoriteItems
                
                // Get recent items (last 7 days)
                val sevenDaysAgo = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7))
                val recentItems = allItems.filter { 
                    it.dateTaken.after(sevenDaysAgo) 
                }.take(10)
                _recent.value = recentItems
                
            } catch (e: Exception) {
                _favorites.value = emptyList()
                _recent.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}