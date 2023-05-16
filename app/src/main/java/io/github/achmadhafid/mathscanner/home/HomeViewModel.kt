package io.github.achmadhafid.mathscanner.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scanResultRepository: ScanResultRepository
) : ViewModel() {

    val uiStateFlow: StateFlow<ScanResults> =
        scanResultRepository.getScanResults()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(FLOW_TIMEOUT),
                initialValue = emptyList()
            )

    fun scan(uri: Uri, storageType: String) {
        viewModelScope.launch {
            scanResultRepository.addScanResult(
                ScanResult(
                    timestamp = Instant.now(),
                    operation = "${Random.nextInt(1, 10)} + ${Random.nextInt(1, 10)}",
                    result = Random.nextInt(),
                    imageUri = uri
                ), storageType
            )
        }
    }

    companion object {
        private const val FLOW_TIMEOUT = 3000L
    }

}
