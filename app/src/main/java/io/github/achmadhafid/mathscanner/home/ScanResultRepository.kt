package io.github.achmadhafid.mathscanner.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Named

class ScanResultRepository @Inject constructor(
    @Named(ScanResultDataSource.TYPE_FILE)
    private val fileDataSource: ScanResultDataSource,
    @Named(ScanResultDataSource.TYPE_DB)
    private val dbDataSource: ScanResultDataSource,
) {

    fun getScanResults(): Flow<ScanResults> =
        fileDataSource.getScanResults()
            .combine(dbDataSource.getScanResults()) { s1, s2 ->
                (s1 + s2).sortedBy { it.timestamp }
            }

}
