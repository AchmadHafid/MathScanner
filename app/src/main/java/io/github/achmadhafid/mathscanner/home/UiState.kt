package io.github.achmadhafid.mathscanner.home

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

data class UiState(
    val scanResults: ScanResults = emptyList(),
    val showSwipeDeleteTutorialDialog: Boolean = false,
    val scanError: ScanError? = null
) {
    @Keep
    @Parcelize
    sealed class ScanError : Parcelable {
        object TextRecognizerError : ScanError()
        data class MathExpressionNotFound(val text: String) : ScanError()
    }
}
