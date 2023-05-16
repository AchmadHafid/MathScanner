package io.github.achmadhafid.mathscanner.home.localdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.achmadhafid.mathscanner.home.localdb.dao.ScanResultDao
import io.github.achmadhafid.mathscanner.home.localdb.entity.ScanResultEntity
import javax.inject.Singleton

@Database(
    entities = [ScanResultEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun scanResultDao(): ScanResultDao

    @Module
    @InstallIn(SingletonComponent::class)
    object DBModule {

        @Singleton
        @Provides
        fun provideDatabase(@ApplicationContext context: Context): LocalDatabase =
            Room.databaseBuilder(
                context,
                LocalDatabase::class.java,
                "AppDB"
            ).fallbackToDestructiveMigration().build()

        @Singleton
        @Provides
        fun provideScanResultDao(database: LocalDatabase): ScanResultDao =
            database.scanResultDao()

    }
}
