package io.github.achmadhafid.mathscanner.swipedeletedialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.achmadhafid.mathscanner.databinding.FragmentDialogSwipeDeleteTutorialBinding

class SwipeDeleteTutorialDialog : BottomSheetDialogFragment() {

    //region View Binding

    private var _viewBinding: FragmentDialogSwipeDeleteTutorialBinding? = null
    private val viewBinding get() = _viewBinding!!

    //endregion
    //region Lifecycle Callback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = FragmentDialogSwipeDeleteTutorialBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //region setup UI

        viewBinding.btnOk.setOnClickListener {
            findNavController().popBackStack()
        }

        //endregion
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

    //endregion

}
