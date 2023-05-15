package io.github.achmadhafid.mathscanner.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

interface ScanResultDataSource {

    fun getScanResults(): Flow<ScanResults>
    suspend fun addScanResult(scanResult: ScanResult)

    companion object {
        const val TYPE_FILE = "file"
        const val TYPE_DB = "db"
    }

}

class ScanResultFileDataSource @Inject constructor() : ScanResultDataSource {

    override fun getScanResults(): Flow<ScanResults> =
        flowOf(DummyScanResults.takeLast(3))

    override suspend fun addScanResult(scanResult: ScanResult) {
        TODO("Not yet implemented")
    }

}

class ScanResultDBDataSource @Inject constructor() : ScanResultDataSource {

    override fun getScanResults(): Flow<ScanResults> =
        flowOf(DummyScanResults.take(2))

    override suspend fun addScanResult(scanResult: ScanResult) {
        TODO("Not yet implemented")
    }

}
