package io.github.achmadhafid.mathscanner.home

import android.net.Uri
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class ScanResult(
    val timestamp: Instant = Instant.now(),
    val operation: String = "",
    val result: Int = 0,
    val sourceUri: Uri? = null
)

val ScanResult.formattedTimestamp: String
    get() = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
        .format(LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault()))

private const val DATE_TIME_FORMAT = "EEEE, yyyy-MM-dd, HH:mm"

val DummyScanResults = listOf(0, 1, 2, 3, 4).map { index ->
    ScanResult(
        timestamp = Instant.now().plusSeconds(60L * index),
        operation = "$index + $index",
        result = index + index
    )
}
