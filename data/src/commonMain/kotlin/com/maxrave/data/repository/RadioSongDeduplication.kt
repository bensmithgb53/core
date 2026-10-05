package com.maxrave.data.repository

import com.maxrave.kotlinytmusicscraper.models.SongItem

/**
 * YouTube may return the same radio track more than once with updated title/artwork/metadata.
 * SongItem's data-class equality then treats those rows as different, but the player identifies
 * them by video ID. Keep the first occurrence so radio order stays intact without replaying it.
 */
internal fun List<SongItem>.distinctRadioSongs(): List<SongItem> = distinctBy { it.id }
