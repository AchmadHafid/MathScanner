package io.github.achmadhafid.mathscanner

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import io.github.achmadhafid.mathscanner.home.ScanResultDBDataSource
import io.github.achmadhafid.mathscanner.home.ScanResultDataSource
import io.github.achmadhafid.mathscanner.home.ScanResultFileDataSource
import jonathanfinerty.once.Once
import javax.inject.Named

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Once.initialise(this)
    }

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

    @InstallIn(ActivityComponent::class)
    @Module
    object ActivityModule {

        @Provides
        fun provideSharedPreferences(@ActivityContext context: Context): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

    }

}
