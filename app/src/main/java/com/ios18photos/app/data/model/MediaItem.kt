package com.ios18photos.app.data.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * Represents a media item (photo or video) in the gallery
 */
@Parcelize
data class MediaItem(
    val id: Long,
    val uri: Uri,
    val displayName: String,
    val dateTaken: Date,
    val dateModified: Date,
    val size: Long,
    val mimeType: String,
    val width: Int,
    val height: Int,
    val duration: Long = 0, // For videos, in milliseconds
    val bucketDisplayName: String, // Album/folder name
    val bucketId: String,
    var isFavorite: Boolean = false,
    var isSelected: Boolean = false
) : Parcelable {
    /**
     * Check if this media item is a video
     */
    val isVideo: Boolean
        get() = mimeType.startsWith("video/")

    /**
     * Check if this media item is a photo
     */
    val isPhoto: Boolean
        get() = mimeType.startsWith("image/")

    /**
     * Get formatted file size
     */
    fun getFormattedSize(): String {
        return when {
            size < 1024 -> "${size} B"
            size < 1024 * 1024 -> "${size / 1024} KB"
            size < 1024 * 1024 * 1024 -> "${size / (1024 * 1024)} MB"
            else -> "${size / (1024 * 1024 * 1024)} GB"
        }
    }

    /**
     * Get formatted dimensions
     */
    fun getFormattedDimensions(): String {
        return "${width} x ${height}"
    }

    /**
     * Get formatted duration for videos
     */
    fun getFormattedDuration(): String {
        if (!isVideo || duration <= 0) return ""
        
        val seconds = duration / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        
        return when {
            hours > 0 -> String.format("%d:%02d:%02d", hours, minutes % 60, seconds % 60)
            else -> String.format("%d:%02d", minutes, seconds % 60)
        }
    }
}

/**
 * Represents an album containing media items
 */
@Parcelize
data class Album(
    val id: String,
    val name: String,
    val coverUri: Uri?,
    val itemCount: Int,
    val isDefault: Boolean = false, // For system albums like Camera Roll, Favorites
    val type: AlbumType = AlbumType.USER_CREATED
) : Parcelable

/**
 * Types of albums in the system
 */
enum class AlbumType {
    USER_CREATED,
    CAMERA_ROLL,
    FAVORITES,
    RECENTLY_DELETED,
    SCREENSHOTS,
    VIDEOS,
    DOWNLOADS
}

/**
 * View types for photo grid display
 */
enum class ViewType {
    YEARS,
    MONTHS,
    DAYS,
    ALL_PHOTOS
}

/**
 * Represents a group of media items by date
 */
data class MediaGroup(
    val title: String,
    val date: Date,
    val items: List<MediaItem>
)