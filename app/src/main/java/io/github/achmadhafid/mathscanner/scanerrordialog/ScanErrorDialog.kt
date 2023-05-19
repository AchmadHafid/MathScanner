package io.github.achmadhafid.mathscanner.scanerrordialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.achmadhafid.mathscanner.R
import io.github.achmadhafid.mathscanner.databinding.FragmentDialogScanErrorBinding
import io.github.achmadhafid.mathscanner.home.UiState

class ScanErrorDialog : BottomSheetDialogFragment() {

    //region Navigation Argument Binding

    private val args by navArgs<ScanErrorDialogArgs>()
    private val scanError: UiState.ScanError
        get() = args.scanError

    //endregion
    //region View Binding

    private var _viewBinding: FragmentDialogScanErrorBinding? = null
    private val viewBinding get() = _viewBinding!!

    //endregion
    //region Lifecycle Callback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = FragmentDialogScanErrorBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //region setup UI

        with(viewBinding) {
            when (scanError) {
                UiState.ScanError.TextRecognizerError -> {
                    animationView.setAnimation(R.raw.animation_text_recognizer_error)
                    tvTitle.text = getText(R.string.text_recognizer_error_title)
                    tvContent.text = getText(R.string.text_recognizer_error_content)
                }

                is UiState.ScanError.MathExpressionNotFound -> {
                    animationView.setAnimation(R.raw.animation_math_expression_not_found)
                    tvTitle.text = getText(R.string.math_expression_not_found_title)
                    (scanError as UiState.ScanError.MathExpressionNotFound).text.let {
                        if (it.isNotEmpty()) {
                            tvContent.text = getText(R.string.math_expression_not_found_content)
                            etText.setText(it)
                            tilText.isVisible = true
                        } else {
                            tvContent.text = getText(R.string.math_expression_not_found_empty_text_content)
                        }
                    }
                }
            }

            btnOk.setOnClickListener {
                findNavController().navigateUp()
            }
        }

        //endregion
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

    //endregion


}
