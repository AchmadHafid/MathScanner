package io.github.achmadhafid.mathscanner.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiStateFlow = MutableStateFlow<ScanResults>(emptyList())
    val uiStateFlow: StateFlow<ScanResults> = _uiStateFlow

    init {
        viewModelScope.launch {
            delay(3 * 1000)
            _uiStateFlow.update { DummyScanResults }
        }
    }

}
