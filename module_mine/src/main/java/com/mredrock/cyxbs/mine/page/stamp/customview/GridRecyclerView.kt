package com.mredrock.cyxbs.mine.page.stamp.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.GridLayoutAnimationController
import android.view.animation.LayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 手动计算GridRv的item数量，让动画正常播放
 * @constructor
 */
class GridRecyclerView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) :
    RecyclerView(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)


    override fun setLayoutAnimation(controller: LayoutAnimationController?) {
        if (controller is GridLayoutAnimationController) {
            super.setLayoutAnimation(controller)
        } else {
            throw ClassCastException("com.mredrock.cyxbs.mine.page.stamp.customview.GridRecyclerView:你必须使用GridLayoutAnimationController")
        }
    }

    override fun attachLayoutAnimationParameters(
        child: View?,
        params: ViewGroup.LayoutParams,
        index: Int,
        count: Int
    ) {
        if (adapter != null && layoutManager is GridLayoutManager) {
            val animationParameters: GridLayoutAnimationController.AnimationParameters?
            if (params.layoutAnimationParameters != null) {
                animationParameters =
                    params.layoutAnimationParameters as GridLayoutAnimationController.AnimationParameters
            } else {
                animationParameters = GridLayoutAnimationController.AnimationParameters()
                params.layoutAnimationParameters = animationParameters
            }

            val spanCount = (layoutManager as GridLayoutManager).spanCount

            animationParameters.apply {
                this.column = count
                this.index = index
                this.columnsCount = spanCount
                this.rowsCount = count / spanCount
            }

            val invertedIndex = count - 1 - index
            animationParameters.column = spanCount - 1 - (invertedIndex % spanCount)
            animationParameters.row = animationParameters.rowsCount - 1 - invertedIndex / spanCount
        } else {
            super.attachLayoutAnimationParameters(child, params, index, count)
        }
    }
}