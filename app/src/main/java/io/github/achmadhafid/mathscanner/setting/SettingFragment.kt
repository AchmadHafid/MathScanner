package io.github.achmadhafid.mathscanner.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.github.achmadhafid.mathscanner.databinding.FragmentSettingBinding
import io.github.achmadhafid.mathscanner.onApplySystemBarWindowInsets

class SettingFragment : Fragment() {

    //region View Binding

    private var _viewBinding: FragmentSettingBinding? = null
    private val viewBinding get() = _viewBinding!!

    //endregion
    //region Lifecycle Callback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = FragmentSettingBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //region setup UI

        setupWindowInset()
        setupToolbar()

        //endregion
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

    //endregion
    //region UI Setup

    private fun setupWindowInset() {
        with(viewBinding) {
            root.onApplySystemBarWindowInsets { topInset, _ ->
                appBar.updatePadding(top = topInset)
            }
        }
    }

    private fun setupToolbar() {
        viewBinding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    //endregion

}
