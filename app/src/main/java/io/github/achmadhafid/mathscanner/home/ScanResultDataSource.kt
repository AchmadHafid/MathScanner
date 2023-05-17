package io.github.achmadhafid.mathscanner.home

import io.github.achmadhafid.mathscanner.home.filestorage.FileStorageManager
import io.github.achmadhafid.mathscanner.home.localdb.dao.ScanResultDao
import io.github.achmadhafid.mathscanner.home.localdb.entity.toScanResultEntity
import io.github.achmadhafid.mathscanner.home.localdb.entity.toScanResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.withIndex
import javax.inject.Inject

interface ScanResultDataSource {

    fun getScanResults(): Flow<ScanResults>
    suspend fun addScanResult(scanResult: ScanResult)
    suspend fun deleteScanResult(scanResult: ScanResult)

    companion object {
        const val TYPE_FILE = "file"
        const val TYPE_DB = "database"
    }

}

class ScanResultFileDataSource @Inject constructor(
    private val fileStorageManager: FileStorageManager
) : ScanResultDataSource {

    private val scanResultsHolder = MutableStateFlow<ScanResults>(emptyList())

    override fun getScanResults(): Flow<ScanResults> =
        flow { emit(fileStorageManager.getAllScanResults()) }
            .combine(scanResultsHolder) { f1, f2 -> f1 to f2 }
            .withIndex()
            .map { if (it.index == 0) it.value.first else it.value.second }

    override suspend fun addScanResult(scanResult: ScanResult) {
        val scanResults = fileStorageManager.insertScanResult(scanResult)
        scanResultsHolder.update { scanResults }
    }

    override suspend fun deleteScanResult(scanResult: ScanResult) {
        val scanResults = fileStorageManager.deleteScanResult(scanResult)
        scanResultsHolder.update { scanResults }
    }

}

class ScanResultDBDataSource @Inject constructor(
    private val scanResultDao: ScanResultDao
) : ScanResultDataSource {

    override fun getScanResults(): Flow<ScanResults> =
        scanResultDao.selectAllAsFlow().map { it.toScanResults() }

    override suspend fun addScanResult(scanResult: ScanResult) {
        scanResultDao.inserts(scanResult.toScanResultEntity())
    }

    override suspend fun deleteScanResult(scanResult: ScanResult) {
        scanResultDao.deletes(scanResult.toScanResultEntity())
    }

}
