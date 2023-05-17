package io.github.achmadhafid.mathscanner.home

import android.net.Uri
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Serializable
data class ScanResult(
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant = Instant.now(),
    val operation: String = "",
    val result: Int = 0,
    @Serializable(with = UriSerializer::class)
    val imageUri: Uri? = null,
    val storageType: String = ""
) {
    class InstantSerializer : KSerializer<Instant> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.LONG)

        override fun serialize(encoder: Encoder, value: Instant) {
            encoder.encodeLong(value.toEpochMilli())
        }

        override fun deserialize(decoder: Decoder): Instant =
            Instant.ofEpochMilli(decoder.decodeLong())

    }

    class UriSerializer : KSerializer<Uri?> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Uri", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Uri?) {
            encoder.encodeString(value?.let { "$it" } ?: "")
        }

        override fun deserialize(decoder: Decoder): Uri? =
            decoder.decodeString().let {
                if (it.isBlank()) null else Uri.parse(it)
            }

    }
}

typealias ScanResults = List<ScanResult>

val ScanResult.formattedTimestamp: String
    get() = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
        .format(LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault()))

private const val DATE_TIME_FORMAT = "EEEE, yyyy-MM-dd, HH:mm"

