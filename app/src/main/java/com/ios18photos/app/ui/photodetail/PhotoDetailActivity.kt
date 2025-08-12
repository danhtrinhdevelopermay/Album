package com.ios18photos.app.ui.photodetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.ios18photos.app.databinding.ActivityPhotoDetailBinding
import com.ios18photos.app.data.model.MediaItem

class PhotoDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityPhotoDetailBinding
    private var mediaItem: MediaItem? = null
    
    companion object {
        private const val EXTRA_MEDIA_ITEM = "extra_media_item"
        
        fun newIntent(context: Context, mediaItem: MediaItem): Intent {
            return Intent(context, PhotoDetailActivity::class.java).apply {
                putExtra(EXTRA_MEDIA_ITEM, mediaItem)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Get media item from intent
        mediaItem = intent.getParcelableExtra(EXTRA_MEDIA_ITEM)
        
        setupUI()
        setupSystemBars()
    }
    
    private fun setupUI() {
        mediaItem?.let { item ->
            // Load the image/video using Glide or appropriate loader
            // TODO: Implement photo viewing with zoom and gestures
            // TODO: Implement video playback if item is video
            // TODO: Add sharing and other actions
        }
        
        // Handle back navigation
        binding.backButton.setOnClickListener {
            finish()
        }
    }
    
    private fun setupSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.systemBarsBehavior = 
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}