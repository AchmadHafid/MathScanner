package io.github.achmadhafid.mathscanner

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.core.view.ViewCompat

@SuppressLint("WrongConstant")
fun View.onApplySystemBarWindowInsets(action: (topInset: Int, bottomInset: Int) -> Unit) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, windowInsets ->
        val topInset = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowInsets.getInsets(WindowInsets.Type.systemBars()).top
        } else {
            @Suppress("DEPRECATION")
            windowInsets.systemWindowInsetTop
        }

        val bottomInset = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowInsets.getInsets(WindowInsets.Type.systemBars()).bottom
        } else {
            @Suppress("DEPRECATION")
            windowInsets.systemWindowInsetBottom
        }

        action(topInset, bottomInset)

        windowInsets
    }
}
