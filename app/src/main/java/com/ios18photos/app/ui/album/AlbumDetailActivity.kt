package com.ios18photos.app.ui.album

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ios18photos.app.databinding.ActivityAlbumDetailBinding
import com.ios18photos.app.data.model.Album
import com.ios18photos.app.ui.adapter.PhotoGridAdapter

class AlbumDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAlbumDetailBinding
    private lateinit var viewModel: AlbumDetailViewModel
    private lateinit var photoAdapter: PhotoGridAdapter
    private var album: Album? = null
    
    companion object {
        private const val EXTRA_ALBUM = "extra_album"
        
        fun newIntent(context: Context, album: Album): Intent {
            return Intent(context, AlbumDetailActivity::class.java).apply {
                putExtra(EXTRA_ALBUM, album)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Get album from intent
        album = intent.getParcelableExtra(EXTRA_ALBUM)
        
        setupViewModel()
        setupUI()
        setupRecyclerView()
        observeData()
    }
    
    private fun setupViewModel() {
        val factory = AlbumDetailViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[AlbumDetailViewModel::class.java]
        album?.let { viewModel.loadAlbumPhotos(it) }
    }
    
    private fun setupUI() {
        album?.let { album ->
            binding.albumTitle.text = album.name
            binding.photoCount.text = "${album.itemCount} photos"
        }
        
        // Handle back navigation
        binding.backButton.setOnClickListener {
            finish()
        }
    }
    
    private fun setupRecyclerView() {
        photoAdapter = PhotoGridAdapter { mediaItem ->
            // Navigate to photo detail
            // TODO: Implement navigation to PhotoDetailActivity
        }
        
        binding.photosRecyclerView.apply {
            layoutManager = GridLayoutManager(this@AlbumDetailActivity, 3)
            adapter = photoAdapter
        }
    }
    
    private fun observeData() {
        viewModel.photos.observe(this) { photos ->
            photoAdapter.submitList(photos)
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            // TODO: Show/hide loading indicator
        }
    }
}