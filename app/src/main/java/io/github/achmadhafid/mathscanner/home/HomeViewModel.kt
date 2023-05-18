package io.github.achmadhafid.mathscanner.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jonathanfinerty.once.Once
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mathScanner: MathScanner,
    private val scanResultRepository: ScanResultRepository
) : ViewModel() {

    private val scanError = MutableStateFlow<UiState.ScanError?>(null)
    private val showSwipeDeleteTutorialDialog = MutableStateFlow(false)

    val uiState: StateFlow<UiState> =
        combine(
            scanResultRepository.getScanResults().onEach { scanResults ->
                showSwipeDeleteTutorialDialog.update { shouldShowSwipeDeleteTutorialDialog(scanResults) }
            },
            showSwipeDeleteTutorialDialog,
            scanError
        ) { scanResults, showTutorial, scanError ->
            UiState(
                scanResults = scanResults,
                showSwipeDeleteTutorialDialog = showTutorial,
                scanError = scanError
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(FLOW_TIMEOUT),
            initialValue = UiState()
        )

    fun scan(uri: Uri, storageType: String) {
        viewModelScope.launch {
            runCatching {
                mathScanner.scan(uri)
            }.onSuccess { (text, result) ->
                if (result != null) {
                    val scanResult = ScanResult(
                        operation = text,
                        result = result,
                        imageUri = uri,
                        storageType = storageType
                    )
                    scanResultRepository.addScanResult(scanResult)
                } else {
                    scanError.update { UiState.ScanError.MathExpressionNotFound(text) }
                }
            }.onFailure {
                scanError.update { UiState.ScanError.TextRecognizerError }
            }
        }
    }

    infix fun delete(scanResult: ScanResult) {
        viewModelScope.launch {
            scanResultRepository.deleteScanResult(scanResult)
        }
    }

    fun onShowScanErrorDialog() {
        scanError.update { null }
    }

    fun onShowSwipeDeleteTutorialDialog() {
        Once.markDone(SWIPE_DELETE_TUTORIAL_TAG)
        showSwipeDeleteTutorialDialog.update { false }
    }

    private fun shouldShowSwipeDeleteTutorialDialog(scanResults: ScanResults) =
        scanResults.size == 1 && !Once.beenDone(Once.THIS_APP_INSTALL, SWIPE_DELETE_TUTORIAL_TAG)

    companion object {
        private const val FLOW_TIMEOUT = 3000L
        const val SWIPE_DELETE_TUTORIAL_TAG = "swipe_to_delete"
    }

}
