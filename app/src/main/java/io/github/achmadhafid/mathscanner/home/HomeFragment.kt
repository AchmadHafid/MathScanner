package io.github.achmadhafid.mathscanner.home

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
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
import io.github.achmadhafid.mathscanner.createPhotoUriWithName
import io.github.achmadhafid.mathscanner.databinding.FragmentHomeBinding
import io.github.achmadhafid.mathscanner.onApplySystemBarWindowInsets
import io.github.achmadhafid.mathscanner.permissiondialog.PermissionDialog
import io.github.achmadhafid.mathscanner.permissiondialog.onPermissionRationaleAskAgain
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onPermissionRationaleAskAgain {
            viewLifecycleOwner.lifecycleScope.launch {
                /** quick workaround to wait for permission dialog rationale dismissed */
                delay(1000)
                takePhoto()
            }
        }
    }

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
        setupFab()

        //endregion
        //region start observers

        observeUiState()

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

    private fun setupFab() {
        viewBinding.fab.setOnClickListener {
//            pickImage()
            takePhoto()
        }
    }

    private fun show(scanResults: ScanResults) {
        scanResultAdapter.submitList(scanResults)
        viewBinding.groupEmpty.isVisible = scanResults.isEmpty()
    }

    //endregion
    //region Observers

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStateFlow.collectLatest {
                    show(it)
                }
            }
        }
    }

    //endregion
    //region Image Picker Helper

    private val imagePickerRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            viewModel.scan(uri, storageType)
        }
    }

    private fun pickImage() {
        imagePickerLauncher.launch(imagePickerRequest)
    }

    //endregion
    //region Photo Picker Helper

    private var photoPickerUri: Uri? = null
    private val photoPickerLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            photoPickerUri?.let {
                viewModel.scan(it, storageType)
            }
        }
    }

    private fun takePhoto() {
        val areRequiredPermissionsGranted = storagePermissions.all {
            ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || areRequiredPermissionsGranted) {
            launchPhotoPicker()
        } else {
            photoPermissionLauncher.launch(storagePermissions)
        }
    }

    private fun launchPhotoPicker() {
        runCatching {
            photoPickerUri = requireContext() createPhotoUriWithName "MathScanner_${Instant.now().toEpochMilli()}"
        }.onSuccess {
            photoPickerLauncher.launch(photoPickerUri)
        }.onFailure {
            Toast.makeText(requireContext(), "Can not take photo: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    //region Permission Helper

    private val storagePermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val photoPermissionLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                launchPhotoPicker()
            } else if (storagePermissions.any { shouldShowRequestPermissionRationale(it) }) {
                showPhotoPermissionRationaleDialog()
            } else showPhotoPermissionsDeniedDialog()
        }

    private fun showPhotoPermissionRationaleDialog() {
        val direction = HomeFragmentDirections.showPermissionDialog(PermissionDialog.Type.Rationale)
        findNavController().navigate(direction)
    }

    private fun showPhotoPermissionsDeniedDialog() {
        val direction = HomeFragmentDirections.showPermissionDialog(PermissionDialog.Type.Denied)
        findNavController().navigate(direction)
    }

    //endregion
    //endregion
    //region Preference Helper

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val storageTypeKey by lazy {
        getString(R.string.preference_key_storage_type)
    }
    private val storageType: String
        get() = sharedPreferences.getString(storageTypeKey, ScanResultDataSource.TYPE_FILE).orEmpty()

    //endregion

}
