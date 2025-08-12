package com.ios18photos.app.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.ios18photos.app.data.model.Album
import com.ios18photos.app.data.model.AlbumType
import com.ios18photos.app.data.model.MediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Repository for accessing media files from device storage
 */
class MediaRepository(private val context: Context) {

    private val contentResolver: ContentResolver = context.contentResolver

    /**
     * Load all media items (photos and videos) from device storage
     */
    suspend fun loadAllMediaItems(): List<MediaItem> = withContext(Dispatchers.IO) {
        val mediaItems = mutableListOf<MediaItem>()
        
        // Load images
        mediaItems.addAll(loadImages())
        
        // Load videos
        mediaItems.addAll(loadVideos())
        
        // Sort by date taken (newest first)
        mediaItems.sortedByDescending { it.dateTaken }
    }

    /**
     * Load all images from MediaStore
     */
    private suspend fun loadImages(): List<MediaItem> = withContext(Dispatchers.IO) {
        val images = mutableListOf<MediaItem>()
        
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
            val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
            val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)
            val bucketDisplayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn) ?: ""
                val dateTaken = cursor.getLong(dateTakenColumn)
                val dateModified = cursor.getLong(dateModifiedColumn)
                val size = cursor.getLong(sizeColumn)
                val mimeType = cursor.getString(mimeTypeColumn) ?: ""
                val width = cursor.getInt(widthColumn)
                val height = cursor.getInt(heightColumn)
                val bucketDisplayName = cursor.getString(bucketDisplayNameColumn) ?: "Unknown"
                val bucketId = cursor.getString(bucketIdColumn) ?: ""

                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                images.add(
                    MediaItem(
                        id = id,
                        uri = uri,
                        displayName = displayName,
                        dateTaken = Date(dateTaken),
                        dateModified = Date(dateModified * 1000),
                        size = size,
                        mimeType = mimeType,
                        width = width,
                        height = height,
                        bucketDisplayName = bucketDisplayName,
                        bucketId = bucketId
                    )
                )
            }
        }
        
        images
    }

    /**
     * Load all videos from MediaStore
     */
    private suspend fun loadVideos(): List<MediaItem> = withContext(Dispatchers.IO) {
        val videos = mutableListOf<MediaItem>()
        
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATE_TAKEN,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.WIDTH,
            MediaStore.Video.Media.HEIGHT,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.BUCKET_ID
        )

        val sortOrder = "${MediaStore.Video.Media.DATE_TAKEN} DESC"

        contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN)
            val dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)
            val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH)
            val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val bucketDisplayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val displayName = cursor.getString(displayNameColumn) ?: ""
                val dateTaken = cursor.getLong(dateTakenColumn)
                val dateModified = cursor.getLong(dateModifiedColumn)
                val size = cursor.getLong(sizeColumn)
                val mimeType = cursor.getString(mimeTypeColumn) ?: ""
                val width = cursor.getInt(widthColumn)
                val height = cursor.getInt(heightColumn)
                val duration = cursor.getLong(durationColumn)
                val bucketDisplayName = cursor.getString(bucketDisplayNameColumn) ?: "Unknown"
                val bucketId = cursor.getString(bucketIdColumn) ?: ""

                val uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                videos.add(
                    MediaItem(
                        id = id,
                        uri = uri,
                        displayName = displayName,
                        dateTaken = Date(dateTaken),
                        dateModified = Date(dateModified * 1000),
                        size = size,
                        mimeType = mimeType,
                        width = width,
                        height = height,
                        duration = duration,
                        bucketDisplayName = bucketDisplayName,
                        bucketId = bucketId
                    )
                )
            }
        }
        
        videos
    }

    /**
     * Load all albums from device storage
     */
    suspend fun loadAlbums(): List<Album> = withContext(Dispatchers.IO) {
        val albums = mutableListOf<Album>()
        val buckets = mutableMapOf<String, Pair<String, MutableList<MediaItem>>>()
        
        // Get all media items to create albums
        val allMedia = loadAllMediaItems()
        
        // Group media by bucket (album)
        allMedia.forEach { mediaItem ->
            val bucketId = mediaItem.bucketId
            val bucketName = mediaItem.bucketDisplayName
            
            if (!buckets.containsKey(bucketId)) {
                buckets[bucketId] = Pair(bucketName, mutableListOf())
            }
            buckets[bucketId]?.second?.add(mediaItem)
        }
        
        // Create album objects
        buckets.forEach { (bucketId, bucketData) ->
            val (bucketName, mediaItems) = bucketData
            if (mediaItems.isNotEmpty()) {
                val coverUri = mediaItems.firstOrNull()?.uri
                val albumType = when (bucketName.toLowerCase(Locale.getDefault())) {
                    "camera" -> AlbumType.CAMERA_ROLL
                    "screenshots" -> AlbumType.SCREENSHOTS
                    "download", "downloads" -> AlbumType.DOWNLOADS
                    else -> AlbumType.USER_CREATED
                }
                
                albums.add(
                    Album(
                        id = bucketId,
                        name = bucketName,
                        coverUri = coverUri,
                        itemCount = mediaItems.size,
                        type = albumType
                    )
                )
            }
        }
        
        // Add default albums
        val favoriteItems = allMedia.filter { it.isFavorite }
        if (favoriteItems.isNotEmpty()) {
            albums.add(
                Album(
                    id = "favorites",
                    name = "Favorites",
                    coverUri = favoriteItems.firstOrNull()?.uri,
                    itemCount = favoriteItems.size,
                    isDefault = true,
                    type = AlbumType.FAVORITES
                )
            )
        }
        
        val videoItems = allMedia.filter { it.isVideo }
        if (videoItems.isNotEmpty()) {
            albums.add(
                Album(
                    id = "videos",
                    name = "Videos",
                    coverUri = videoItems.firstOrNull()?.uri,
                    itemCount = videoItems.size,
                    isDefault = true,
                    type = AlbumType.VIDEOS
                )
            )
        }
        
        albums.sortedBy { it.name }
    }

    /**
     * Search media items by query
     */
    suspend fun searchMediaItems(query: String): List<MediaItem> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        
        val allItems = loadAllMediaItems()
        allItems.filter { mediaItem ->
            mediaItem.displayName.contains(query, ignoreCase = true) ||
            mediaItem.bucketDisplayName.contains(query, ignoreCase = true)
        }
    }

    /**
     * Get media items for a specific album
     */
    suspend fun getMediaItemsForAlbum(albumId: String): List<MediaItem> = withContext(Dispatchers.IO) {
        val allItems = loadAllMediaItems()
        
        when (albumId) {
            "favorites" -> allItems.filter { it.isFavorite }
            "videos" -> allItems.filter { it.isVideo }
            else -> allItems.filter { it.bucketId == albumId }
        }
    }
}