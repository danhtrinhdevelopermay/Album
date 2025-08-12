package com.ios18photos.app.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ios18photos.app.databinding.FragmentSearchBinding
import com.ios18photos.app.ui.adapter.PhotoGridAdapter
import com.ios18photos.app.ui.adapter.SearchSuggestionAdapter

/**
 * Fragment for searching photos and videos
 */
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchResultsAdapter: PhotoGridAdapter
    private lateinit var suggestionsAdapter: SearchSuggestionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupSearchBar()
        setupRecyclerViews()
        observeViewModel()
        
        // Load search suggestions
        viewModel.loadSuggestions()
    }

    /**
     * Setup search bar with text change listener
     */
    private fun setupSearchBar() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.trim() ?: ""
                viewModel.search(query)
            }
        })
    }

    /**
     * Setup RecyclerViews for search results and suggestions
     */
    private fun setupRecyclerViews() {
        searchResultsAdapter = PhotoGridAdapter { mediaItem ->
            // Handle photo click - navigate to photo detail
            // TODO: Implement navigation to photo detail activity
        }

        suggestionsAdapter = SearchSuggestionAdapter { suggestion ->
            binding.searchEditText.setText(suggestion)
        }

        binding.searchResultsRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = searchResultsAdapter
        }

        binding.suggestionsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = suggestionsAdapter
        }
    }

    /**
     * Observe ViewModel changes
     */
    private fun observeViewModel() {
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            searchResultsAdapter.submitList(results)
            
            val hasResults = results.isNotEmpty()
            val hasQuery = binding.searchEditText.text?.isNotBlank() == true
            
            binding.searchResultsRecyclerView.visibility = if (hasResults) View.VISIBLE else View.GONE
            binding.emptySearchState.visibility = if (!hasResults && hasQuery) View.VISIBLE else View.GONE
            binding.searchSuggestions.visibility = if (!hasQuery) View.VISIBLE else View.GONE
        }

        viewModel.suggestions.observe(viewLifecycleOwner) { suggestions ->
            suggestionsAdapter.submitList(suggestions)
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