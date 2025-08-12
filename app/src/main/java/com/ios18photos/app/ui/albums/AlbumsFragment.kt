package com.ios18photos.app.ui.albums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.ios18photos.app.R
import com.ios18photos.app.databinding.FragmentAlbumsBinding
import com.ios18photos.app.ui.adapter.AlbumAdapter

/**
 * Fragment displaying user albums and default system albums
 */
class AlbumsFragment : Fragment() {

    private var _binding: FragmentAlbumsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AlbumsViewModel by viewModels()
    private lateinit var myAlbumsAdapter: AlbumAdapter
    private lateinit var defaultAlbumsAdapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerViews()
        setupFab()
        observeViewModel()
        
        // Load albums
        viewModel.loadAlbums()
    }

    /**
     * Setup RecyclerViews for my albums and default albums
     */
    private fun setupRecyclerViews() {
        myAlbumsAdapter = AlbumAdapter { album ->
            // Handle album click - navigate to album detail
            // TODO: Implement navigation to album detail activity
        }

        defaultAlbumsAdapter = AlbumAdapter { album ->
            // Handle album click - navigate to album detail
            // TODO: Implement navigation to album detail activity
        }

        binding.myAlbumsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = myAlbumsAdapter
        }

        binding.defaultAlbumsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = defaultAlbumsAdapter
        }
    }

    /**
     * Setup FAB for creating new album
     */
    private fun setupFab() {
        binding.fabCreateAlbum.setOnClickListener {
            showCreateAlbumDialog()
        }
    }

    /**
     * Show dialog for creating new album
     */
    private fun showCreateAlbumDialog() {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_create_album, null)
        val editText = dialogView.findViewById<TextInputEditText>(R.id.album_name_edit_text)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.create_album)
            .setView(dialogView)
            .setPositiveButton(R.string.create) { _, _ ->
                val albumName = editText.text?.toString()?.trim()
                if (!albumName.isNullOrBlank()) {
                    viewModel.createAlbum(albumName)
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    /**
     * Observe ViewModel changes
     */
    private fun observeViewModel() {
        viewModel.myAlbums.observe(viewLifecycleOwner) { albums ->
            myAlbumsAdapter.submitList(albums)
        }

        viewModel.defaultAlbums.observe(viewLifecycleOwner) { albums ->
            defaultAlbumsAdapter.submitList(albums)
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