package com.maxrave.logger

import co.touchlab.kermit.Logger

object Logger {
    private val logger = Logger
    private var playbackDiagnosticSink: ((String, String, String, Throwable?) -> Unit)? = null
    private var playbackDiagnosticSnapshot: (() -> String)? = null

    // Tags suppressed at all log levels. Add a tag here to silence its logs globally.
    private val mutedTags =
        setOf(
            "DiscordWebSocket",
        )

    private fun isMuted(tag: String): Boolean = tag in mutedTags

    /** Install an optional app-local playback log collector (enabled by the Android debug build). */
    fun installPlaybackDiagnostics(
        sink: (String, String, String, Throwable?) -> Unit,
        snapshot: () -> String,
    ) {
        playbackDiagnosticSink = sink
        playbackDiagnosticSnapshot = snapshot
    }

    fun hasPlaybackDiagnostics(): Boolean = playbackDiagnosticSnapshot != null

    fun playbackDiagnosticsSnapshot(): String =
        playbackDiagnosticSnapshot?.invoke() ?: "Playback diagnostics are only available in the debug build."

    private fun capturePlaybackDiagnostic(
        severity: String,
        tag: String,
        message: String,
        throwable: Throwable? = null,
    ) {
        try {
            playbackDiagnosticSink?.invoke(severity, tag, message, throwable)
        } catch (_: Throwable) {
            // Diagnostics must never interfere with normal app logging or playback.
        }
    }

    fun d(
        tag: String,
        message: String,
    ) {
        if (isMuted(tag)) return
        capturePlaybackDiagnostic("DEBUG", tag, message)
        logger.d(
            tag = tag,
            message = {
                message
            },
        )
    }

    fun i(
        tag: String,
        message: String,
    ) {
        if (isMuted(tag)) return
        capturePlaybackDiagnostic("INFO", tag, message)
        logger.i(tag = tag, message = { message })
    }

    fun w(
        tag: String,
        message: String,
    ) {
        if (isMuted(tag)) return
        capturePlaybackDiagnostic("WARN", tag, message)
        logger.w(tag = tag, message = { message })
    }

    fun e(
        tag: String,
        message: String,
        e: Throwable? = null,
    ) {
        if (isMuted(tag)) return
        capturePlaybackDiagnostic("ERROR", tag, message, e)
        logger.e(throwable = e, tag = tag, message = { message })
    }
}

enum class LogLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR,
}
