package com.maxrave.data.repository

import com.maxrave.kotlinytmusicscraper.models.SongItem
import kotlin.test.Test
import kotlin.test.assertEquals

class RadioSongDeduplicationTest {
    @Test
    fun `deduplicates the same video even when YouTube changes its metadata`() {
        val first = song(id = "same-video", title = "Sorry")
        val updatedMetadata = song(id = "same-video", title = "Sorry (updated metadata)")
        val next = song(id = "another-video", title = "Love Yourself")

        assertEquals(
            listOf(first, next),
            listOf(first, updatedMetadata, next).distinctRadioSongs(),
        )
    }

    @Test
    fun `keeps the first-seen order and distinct tracks`() {
        val songs = listOf(
            song(id = "a", title = "A"),
            song(id = "b", title = "B"),
            song(id = "a", title = "A again"),
            song(id = "c", title = "C"),
        )

        assertEquals(listOf("a", "b", "c"), songs.distinctRadioSongs().map { it.id })
    }

    private fun song(id: String, title: String) =
        SongItem(
            id = id,
            title = title,
            artists = emptyList(),
            thumbnail = "",
        )
}
