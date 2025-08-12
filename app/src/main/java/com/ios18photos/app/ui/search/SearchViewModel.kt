package com.ios18photos.app.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ios18photos.app.data.model.MediaItem
import com.ios18photos.app.data.repository.MediaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for SearchFragment
 */
class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val mediaRepository = MediaRepository(application)

    private val _searchResults = MutableLiveData<List<MediaItem>>()
    val searchResults: LiveData<List<MediaItem>> = _searchResults

    private val _suggestions = MutableLiveData<List<String>>()
    val suggestions: LiveData<List<String>> = _suggestions

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var searchJob: Job? = null

    /**
     * Search for media items with debouncing
     */
    fun search(query: String) {
        searchJob?.cancel()
        
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            // Debounce search for better performance
            delay(300)
            
            _isLoading.value = true
            try {
                val results = mediaRepository.searchMediaItems(query)
                _searchResults.value = results
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Load search suggestions
     */
    fun loadSuggestions() {
        viewModelScope.launch {
            try {
                // In a real app, these would come from search history or popular searches
                val suggestions = listOf(
                    "Summer vacation",
                    "Family photos",
                    "Screenshots",
                    "Videos",
                    "Camera",
                    "Nature",
                    "Portraits"
                )
                _suggestions.value = suggestions
            } catch (e: Exception) {
                _suggestions.value = emptyList()
            }
        }
    }
}