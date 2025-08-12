package com.ios18photos.app.ui.foryou

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ios18photos.app.databinding.FragmentForYouBinding
import com.ios18photos.app.ui.adapter.PhotoSmallAdapter

/**
 * Fragment showing curated content like favorites and recent photos
 */
class ForYouFragment : Fragment() {

    private var _binding: FragmentForYouBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ForYouViewModel by viewModels()
    private lateinit var favoritesAdapter: PhotoSmallAdapter
    private lateinit var recentAdapter: PhotoSmallAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerViews()
        observeViewModel()
        
        // Load curated content
        viewModel.loadCuratedContent()
    }

    /**
     * Setup RecyclerViews for favorites and recent photos
     */
    private fun setupRecyclerViews() {
        favoritesAdapter = PhotoSmallAdapter { mediaItem ->
            // Handle photo click - navigate to photo detail
            // TODO: Implement navigation to photo detail activity
        }

        recentAdapter = PhotoSmallAdapter { mediaItem ->
            // Handle photo click - navigate to photo detail
            // TODO: Implement navigation to photo detail activity
        }

        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = favoritesAdapter
        }

        binding.recentRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = recentAdapter
        }
    }

    /**
     * Observe ViewModel changes
     */
    private fun observeViewModel() {
        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            favoritesAdapter.submitList(favorites)
        }

        viewModel.recent.observe(viewLifecycleOwner) { recent ->
            recentAdapter.submitList(recent)
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