package io.github.achmadhafid.mathscanner.permissiondialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.achmadhafid.mathscanner.R
import io.github.achmadhafid.mathscanner.databinding.FragmentDialogPermissionBinding

class PermissionDialog : BottomSheetDialogFragment() {

    //region Navigation Argument Binding

    private val args by navArgs<PermissionDialogArgs>()
    private val type: Type
        get() = args.type

    //endregion
    //region View Binding

    private var _viewBinding: FragmentDialogPermissionBinding? = null
    private val viewBinding get() = _viewBinding!!

    //endregion
    //region Lifecycle Callback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = FragmentDialogPermissionBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //region setup UI

        with(viewBinding) {
            when (type) {
                Type.Rationale -> {
                    animationView.setAnimation(R.raw.animation_photo_permission_rationale)
                    tvTitle.text = getText(R.string.permission_rationale_title)
                    tvContent.text = getText(R.string.permission_rationale_content_photo)
                    btnOk.setOnClickListener {
                        setPermissionRationaleAskAgainResult()
                        findNavController().navigateUp()
                    }
                }

                Type.Denied -> {
                    animationView.setAnimation(R.raw.animation_photo_permission_denied)
                    tvTitle.text = getText(R.string.permission_denied_title)
                    tvContent.text = getText(R.string.permission_denied_content_photo)
                    btnOk.setOnClickListener {
                        findNavController().navigateUp()
                    }
                }
            }
        }

        //endregion
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

    //endregion

    enum class Type {
        Rationale, Denied
    }

}

//region Fragment Result Helper

internal fun Fragment.onPermissionRationaleAskAgain(action: () -> Unit) {
    setFragmentResultListener(REQUEST_KEY) { _, _ -> action() }
}

private fun PermissionDialog.setPermissionRationaleAskAgainResult() {
    setFragmentResult(REQUEST_KEY, bundleOf())
}

private const val REQUEST_KEY = "on_rationale_ask_again"

//endregion
