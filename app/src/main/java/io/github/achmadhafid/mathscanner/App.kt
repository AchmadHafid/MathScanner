package io.github.achmadhafid.mathscanner

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import io.github.achmadhafid.mathscanner.home.ScanResultDBDataSource
import io.github.achmadhafid.mathscanner.home.ScanResultDataSource
import io.github.achmadhafid.mathscanner.home.ScanResultFileDataSource
import javax.inject.Named

@HiltAndroidApp
class App : Application() {

    @InstallIn(SingletonComponent::class)
    @Module
    object AppModule {

        @Provides
        @Named(ScanResultDataSource.TYPE_FILE)
        fun providesScanResultFileDataSource(implementation: ScanResultFileDataSource): ScanResultDataSource =
            implementation

        @Provides
        @Named(ScanResultDataSource.TYPE_DB)
        fun providesScanResultDBDataSource(implementation: ScanResultDBDataSource): ScanResultDataSource =
            implementation

    }

}
