package com.ios18photos.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ios18photos.app.R
import com.ios18photos.app.data.model.MediaItem
import com.ios18photos.app.databinding.ItemPhotoGridBinding

/**
 * Adapter for displaying photos in grid layout
 */
class PhotoGridAdapter(
    private val onItemClick: (MediaItem) -> Unit
) : ListAdapter<MediaItem, PhotoGridAdapter.PhotoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPhotoGridBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PhotoViewHolder(
        private val binding: ItemPhotoGridBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(mediaItem: MediaItem) {
            // Load image using Glide
            Glide.with(binding.root.context)
                .load(mediaItem.uri)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.color.background_secondary)
                .error(R.color.background_secondary)
                .into(binding.photoImage)

            // Show video indicator if it's a video
            binding.videoIndicator.visibility = if (mediaItem.isVideo) View.VISIBLE else View.GONE

            // Show favorite indicator if it's a favorite
            binding.favoriteIndicator.visibility = if (mediaItem.isFavorite) View.VISIBLE else View.GONE

            // Show selection overlay if selected
            binding.selectionOverlay.visibility = if (mediaItem.isSelected) View.VISIBLE else View.GONE

            // Handle click
            binding.root.setOnClickListener {
                onItemClick(mediaItem)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MediaItem>() {
        override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
            return oldItem == newItem
        }
    }
}

/**
 * Adapter for small photo thumbnails (used in For You tab)
 */
class PhotoSmallAdapter(
    private val onItemClick: (MediaItem) -> Unit
) : ListAdapter<MediaItem, PhotoSmallAdapter.PhotoSmallViewHolder>(PhotoGridAdapter.DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoSmallViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo_small, parent, false)
        return PhotoSmallViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoSmallViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PhotoSmallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photoImage = itemView.findViewById<android.widget.ImageView>(R.id.photo_image)
        private val videoIndicator = itemView.findViewById<android.widget.ImageView>(R.id.video_indicator)

        fun bind(mediaItem: MediaItem) {
            Glide.with(itemView.context)
                .load(mediaItem.uri)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.color.background_secondary)
                .error(R.color.background_secondary)
                .into(photoImage)

            videoIndicator.visibility = if (mediaItem.isVideo) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                onItemClick(mediaItem)
            }
        }
    }
}