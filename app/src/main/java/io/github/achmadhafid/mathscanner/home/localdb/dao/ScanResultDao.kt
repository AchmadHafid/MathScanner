package io.github.achmadhafid.mathscanner.home.localdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.achmadhafid.mathscanner.home.localdb.entity.ScanResultEntities
import io.github.achmadhafid.mathscanner.home.localdb.entity.ScanResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserts(vararg item: ScanResultEntity)

    @Query("SELECT * FROM ScanResultEntity")
    fun selectAllAsFlow(): Flow<ScanResultEntities>

}
