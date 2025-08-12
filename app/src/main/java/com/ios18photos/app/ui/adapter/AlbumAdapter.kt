package com.ios18photos.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ios18photos.app.R
import com.ios18photos.app.data.model.Album
import com.ios18photos.app.databinding.ItemAlbumBinding

/**
 * Adapter for displaying albums
 */
class AlbumAdapter(
    private val onItemClick: (Album) -> Unit
) : ListAdapter<Album, AlbumAdapter.AlbumViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemAlbumBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AlbumViewHolder(
        private val binding: ItemAlbumBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(album: Album) {
            binding.albumName.text = album.name
            binding.albumCount.text = binding.root.context.resources.getQuantityString(
                R.plurals.item_count,
                album.itemCount,
                album.itemCount
            )

            // Load cover image
            if (album.coverUri != null) {
                Glide.with(binding.root.context)
                    .load(album.coverUri)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.color.background_secondary)
                    .error(R.color.background_secondary)
                    .into(binding.albumThumbnail)
            } else {
                // Set default album icon based on type
                val defaultIcon = when (album.type) {
                    com.ios18photos.app.data.model.AlbumType.FAVORITES -> R.drawable.ic_favorite
                    com.ios18photos.app.data.model.AlbumType.VIDEOS -> R.drawable.ic_play_arrow
                    com.ios18photos.app.data.model.AlbumType.SCREENSHOTS -> R.drawable.ic_photos_tab
                    com.ios18photos.app.data.model.AlbumType.RECENTLY_DELETED -> R.drawable.ic_delete
                    else -> R.drawable.ic_albums_tab
                }
                binding.albumThumbnail.setImageResource(defaultIcon)
            }

            binding.root.setOnClickListener {
                onItemClick(album)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }
}