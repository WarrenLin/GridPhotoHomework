package com.example.gridphotohomework.photo.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class GridVerticalSpacingItemDecoration(private val spacingInPx: Int, private val spanCount: Int) :
    ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = spacingInPx
        outRect.right = spacingInPx
        outRect.top = spacingInPx
        outRect.bottom = spacingInPx
        val totalCount = state.itemCount
        val totalRow = totalCount / spanCount + 1
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemRow = itemPosition / spanCount + 1
        if (itemRow != totalRow) { //last row keep bottom padding.
            outRect.bottom = 0
        }
        if (itemPosition % spanCount == spanCount - 1) { //last column padding right.
            outRect.right = spacingInPx
        } else {
            outRect.right = 0
        }
    }

}