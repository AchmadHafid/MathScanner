package io.github.achmadhafid.mathscanner

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.WindowInsets
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

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

fun RecyclerView.onRightSwiped(action: (position: Int) -> Unit) {
    ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            action(viewHolder.adapterPosition)
        }
    }).attachToRecyclerView(this)
}
