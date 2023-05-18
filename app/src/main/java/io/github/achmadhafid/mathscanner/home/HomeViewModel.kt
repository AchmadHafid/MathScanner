package io.github.achmadhafid.mathscanner.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mathScanner: MathScanner,
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
            runCatching {
                mathScanner.scan(uri)
            }.onSuccess { (operation, result) ->
                if (result != null) {
                    val scanResult = ScanResult(
                        operation = operation,
                        result = result,
                        imageUri = uri,
                        storageType = storageType
                    )
                    scanResultRepository.addScanResult(scanResult)
                } else {
                    TODO("show error dialog")
                }
            }.onFailure {
                TODO("show error dialog")
            }
        }
    }

    fun delete(scanResult: ScanResult) {
        viewModelScope.launch {
            scanResultRepository.deleteScanResult(scanResult)
        }
    }

    companion object {
        private const val FLOW_TIMEOUT = 3000L
    }

}
