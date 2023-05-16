package io.github.achmadhafid.mathscanner.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import javax.inject.Inject

interface ScanResultDataSource {

    fun getScanResults(): Flow<ScanResults>
    suspend fun addScanResult(scanResult: ScanResult)

    companion object {
        const val TYPE_FILE = "file"
        const val TYPE_DB = "database"
    }

}

class ScanResultFileDataSource @Inject constructor() : ScanResultDataSource {

    private val scanResultsHolder = MutableStateFlow<ScanResults>(emptyList())

    override fun getScanResults(): Flow<ScanResults> =
        scanResultsHolder

    override suspend fun addScanResult(scanResult: ScanResult) {
        scanResultsHolder.getAndUpdate { it + scanResult }
    }

}

class ScanResultDBDataSource @Inject constructor() : ScanResultDataSource {

    private val scanResultsHolder = MutableStateFlow<ScanResults>(emptyList())

    override fun getScanResults(): Flow<ScanResults> =
        scanResultsHolder

    override suspend fun addScanResult(scanResult: ScanResult) {
        scanResultsHolder.getAndUpdate { it + scanResult }
    }

}
