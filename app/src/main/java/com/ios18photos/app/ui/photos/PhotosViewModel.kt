package com.ios18photos.app.ui.photos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ios18photos.app.data.model.MediaItem
import com.ios18photos.app.data.model.ViewType
import com.ios18photos.app.data.repository.MediaRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for PhotosFragment
 */
class PhotosViewModel(application: Application) : AndroidViewModel(application) {

    private val mediaRepository = MediaRepository(application)

    private val _mediaItems = MutableLiveData<List<MediaItem>>()
    val mediaItems: LiveData<List<MediaItem>> = _mediaItems

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _currentViewType = MutableLiveData<ViewType>(ViewType.ALL_PHOTOS)
    val currentViewType: LiveData<ViewType> = _currentViewType

    private var allMediaItems: List<MediaItem> = emptyList()

    /**
     * Load all photos from device storage
     */
    fun loadPhotos() {
        _isLoading.value = true
        
        viewModelScope.launch {
            try {
                allMediaItems = mediaRepository.loadAllMediaItems()
                updateDisplayedItems()
            } catch (e: Exception) {
                // Handle error
                _mediaItems.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Set the current view type and update displayed items
     */
    fun setViewType(viewType: ViewType) {
        _currentViewType.value = viewType
        updateDisplayedItems()
    }

    /**
     * Update displayed items based on current view type
     */
    private fun updateDisplayedItems() {
        val viewType = _currentViewType.value ?: ViewType.ALL_PHOTOS
        
        val filteredItems = when (viewType) {
            ViewType.ALL_PHOTOS -> allMediaItems
            ViewType.DAYS -> groupByDays(allMediaItems)
            ViewType.MONTHS -> groupByMonths(allMediaItems)
            ViewType.YEARS -> groupByYears(allMediaItems)
        }
        
        _mediaItems.value = filteredItems
    }

    /**
     * Group media items by days (recent items with higher priority)
     */
    private fun groupByDays(items: List<MediaItem>): List<MediaItem> {
        // For simplicity, return recent items (last 30 days worth)
        val thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000)
        return items.filter { it.dateTaken.time > thirtyDaysAgo }
    }

    /**
     * Group media items by months (sample representative items)
     */
    private fun groupByMonths(items: List<MediaItem>): List<MediaItem> {
        // Group by month and take first item from each month
        return items.groupBy { 
            val calendar = java.util.Calendar.getInstance()
            calendar.time = it.dateTaken
            "${calendar.get(java.util.Calendar.YEAR)}-${calendar.get(java.util.Calendar.MONTH)}"
        }.values.mapNotNull { it.firstOrNull() }
    }

    /**
     * Group media items by years (sample representative items)
     */
    private fun groupByYears(items: List<MediaItem>): List<MediaItem> {
        // Group by year and take first item from each year
        return items.groupBy { 
            val calendar = java.util.Calendar.getInstance()
            calendar.time = it.dateTaken
            calendar.get(java.util.Calendar.YEAR)
        }.values.mapNotNull { it.firstOrNull() }
    }
}