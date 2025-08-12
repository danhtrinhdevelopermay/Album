package com.ios18photos.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ios18photos.app.databinding.ItemSearchSuggestionBinding

/**
 * Adapter for search suggestions
 */
class SearchSuggestionAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<String, SearchSuggestionAdapter.SuggestionViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val binding = ItemSearchSuggestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SuggestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SuggestionViewHolder(
        private val binding: ItemSearchSuggestionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(suggestion: String) {
            binding.suggestionText.text = suggestion

            binding.root.setOnClickListener {
                onItemClick(suggestion)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}