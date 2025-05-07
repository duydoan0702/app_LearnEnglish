package com.mobiai.app.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemSpacingDecoration(
    private val spacingHorizontal: Int,
    private val spacingVertical: Int
) : RecyclerView.ItemDecoration() {

    // Override phương thức này để xác định khoảng cách giữa các item.
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        // Áp dụng khoảng cách cho cả top, left, right và bottom của item
        outRect.right = spacingHorizontal
        outRect.left = spacingHorizontal
        outRect.top = spacingVertical
        outRect.bottom = spacingVertical
    }
}
