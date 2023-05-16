package io.github.achmadhafid.mathscanner.home.localdb.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.achmadhafid.mathscanner.home.ScanResult
import io.github.achmadhafid.mathscanner.home.ScanResults
import java.time.Instant

@Entity
data class ScanResultEntity(
    @PrimaryKey
    val timestamp: Long = 0L,
    val operation: String = "",
    val result: Int = 0,
    val imageUri: String = "",
    val storageType: String = ""
)

typealias ScanResultEntities = List<ScanResultEntity>

fun ScanResultEntity.toScanResult() =
    ScanResult(
        timestamp = Instant.ofEpochMilli(timestamp),
        operation = operation,
        result = result,
        imageUri = if (imageUri.isBlank()) null else Uri.parse(imageUri),
        storageType = storageType
    )

fun ScanResultEntities.toScanResults() =
    map { it.toScanResult() }

fun ScanResult.toScanResultEntity() =
    ScanResultEntity(
        timestamp = timestamp.toEpochMilli(),
        operation = operation,
        result = result,
        imageUri = imageUri?.let { "$it" } ?: "",
        storageType = storageType
    )

fun ScanResults.toScanResultEntities() =
    map { it.toScanResultEntity() }
