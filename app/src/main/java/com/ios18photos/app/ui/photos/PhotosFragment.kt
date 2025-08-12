package com.ios18photos.app.ui.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.ios18photos.app.R
import com.ios18photos.app.data.model.ViewType
import com.ios18photos.app.databinding.FragmentPhotosBinding
import com.ios18photos.app.ui.adapter.PhotoGridAdapter

/**
 * Fragment displaying photos in different view modes (Years, Months, Days, All Photos)
 */
class PhotosFragment : Fragment() {

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotosViewModel by viewModels()
    private lateinit var photoAdapter: PhotoGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupTabLayout()
        observeViewModel()
        
        // Load photos
        viewModel.loadPhotos()
    }

    /**
     * Setup RecyclerView with grid layout
     */
    private fun setupRecyclerView() {
        val spanCount = when (viewModel.currentViewType.value) {
            ViewType.YEARS -> 1
            ViewType.MONTHS -> 2
            ViewType.DAYS -> 3
            ViewType.ALL_PHOTOS -> 4
            else -> 4
        }
        
        photoAdapter = PhotoGridAdapter { mediaItem ->
            // Handle photo click - navigate to photo detail
            // TODO: Implement navigation to photo detail activity
        }
        
        binding.photosRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = photoAdapter
        }
    }

    /**
     * Setup tab layout for view type selection
     */
    private fun setupTabLayout() {
        binding.viewTypeTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val viewType = when (tab?.position) {
                    0 -> ViewType.YEARS
                    1 -> ViewType.MONTHS
                    2 -> ViewType.DAYS
                    3 -> ViewType.ALL_PHOTOS
                    else -> ViewType.ALL_PHOTOS
                }
                
                viewModel.setViewType(viewType)
                updateGridSpanCount(viewType)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    /**
     * Update grid span count based on view type
     */
    private fun updateGridSpanCount(viewType: ViewType) {
        val spanCount = when (viewType) {
            ViewType.YEARS -> 1
            ViewType.MONTHS -> 2
            ViewType.DAYS -> 3
            ViewType.ALL_PHOTOS -> 4
        }
        
        (binding.photosRecyclerView.layoutManager as GridLayoutManager).spanCount = spanCount
        photoAdapter.notifyDataSetChanged()
    }

    /**
     * Observe ViewModel changes
     */
    private fun observeViewModel() {
        viewModel.mediaItems.observe(viewLifecycleOwner) { mediaItems ->
            photoAdapter.submitList(mediaItems)
            
            // Show/hide empty state
            binding.emptyState.visibility = if (mediaItems.isEmpty()) View.VISIBLE else View.GONE
            binding.photosRecyclerView.visibility = if (mediaItems.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}