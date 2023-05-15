package io.github.achmadhafid.mathscanner.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    scanResultRepository: ScanResultRepository
) : ViewModel() {

    val uiStateFlow: StateFlow<ScanResults> =
        scanResultRepository.getScanResults()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(FLOW_TIMEOUT),
                initialValue = emptyList()
            )

    companion object {
        private const val FLOW_TIMEOUT = 3000L
    }

}
