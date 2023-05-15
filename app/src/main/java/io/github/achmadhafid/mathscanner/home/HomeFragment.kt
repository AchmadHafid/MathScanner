package io.github.achmadhafid.mathscanner.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.achmadhafid.mathscanner.R
import io.github.achmadhafid.mathscanner.databinding.FragmentHomeBinding
import io.github.achmadhafid.mathscanner.onApplySystemBarWindowInsets
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    //region View Binding

    private var _viewBinding: FragmentHomeBinding? = null
    private val viewBinding get() = _viewBinding!!

    //endregion
    //region View Model Binding

    private val viewModel by viewModels<HomeViewModel>()

    //endregion
    //region List Adapter

    @Inject
    lateinit var scanResultAdapter: ScanResultAdapter

    //endregion
    //region Lifecycle Callback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //region setup UI

        setupWindowInset()
        setupToolbar()
        setupRecyclerView()

        //endregion
        //region start observers

        observeViewState()

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
            root.onApplySystemBarWindowInsets { topInset, bottomInset ->
                appBar.updatePadding(top = topInset)
                fab.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                    val originalMargin = resources.getDimensionPixelOffset(R.dimen.large)
                    setMargins(0, 0, originalMargin, originalMargin + bottomInset)
                }
            }
        }
    }

    private fun setupToolbar() {
        viewBinding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_setting -> true.also {
                    findNavController().navigate(HomeFragmentDirections.openSetting())
                }

                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        viewBinding.rvScanResults.apply {
            adapter = scanResultAdapter
        }
    }

    private fun show(scanResults: ScanResults) {
        scanResultAdapter.submitList(scanResults)
        viewBinding.groupEmpty.isVisible = scanResults.isEmpty()
    }

    //endregion
    //region Observers

    private fun observeViewState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStateFlow.collectLatest {
                    show(it)
                }
            }
        }
    }

    //endregion

}
