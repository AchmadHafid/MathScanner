package io.github.achmadhafid.mathscanner.home.filestorage

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.achmadhafid.mathscanner.home.ScanResult
import io.github.achmadhafid.mathscanner.home.ScanResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileStorageManager @Inject constructor(@ApplicationContext private val context: Context) {

    //region Crypto Setting

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val file = File(context.filesDir, "scan_results")

    private val encryptedFile = EncryptedFile.Builder(
        context,
        file,
        masterKey,
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()

    //endregion

    suspend fun insertScanResult(scanResult: ScanResult): ScanResults =
        (getAllScanResults() + scanResult).also {
            writeToFile(Json.encodeToString(it))
        }

    suspend fun deleteScanResult(scanResult: ScanResult): ScanResults =
        (getAllScanResults() - scanResult).also {
            writeToFile(Json.encodeToString(it))
        }

    suspend fun getAllScanResults(): ScanResults =
        runCatching {
            Json.decodeFromString<ScanResults>(readFromFile())
        }.getOrDefault(emptyList())

    private suspend fun writeToFile(scanResultsJson: String) = withContext(Dispatchers.IO) {
        /** Workaround since EncryptedFile will not allow us to overwrite existing file */
        if (file.exists()) {
            file.delete()
        }
        encryptedFile.openFileOutput().use {
            it.write(scanResultsJson.toByteArray())
        }
    }

    private suspend fun readFromFile(): String = withContext(Dispatchers.IO) {
        encryptedFile.openFileInput()
            .bufferedReader()
            .use { it.readText() }
    }

}
