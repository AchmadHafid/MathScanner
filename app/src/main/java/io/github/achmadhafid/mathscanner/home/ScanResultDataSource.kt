package io.github.achmadhafid.mathscanner.home

import io.github.achmadhafid.mathscanner.home.localdb.dao.ScanResultDao
import io.github.achmadhafid.mathscanner.home.localdb.entity.toScanResultEntity
import io.github.achmadhafid.mathscanner.home.localdb.entity.toScanResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.map
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

class ScanResultDBDataSource @Inject constructor(
    private val scnDao: ScanResultDao
) : ScanResultDataSource {

    override fun getScanResults(): Flow<ScanResults> =
        scnDao.selectAllAsFlow().map { it.toScanResults() }

    override suspend fun addScanResult(scanResult: ScanResult) {
        scnDao.inserts(scanResult.toScanResultEntity())
    }

}
