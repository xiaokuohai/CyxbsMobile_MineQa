package com.mredrock.cyxbs.mine.page.stamp.fragment

import android.content.Context
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mredrock.cyxbs.common.utils.extensions.onClick
import com.mredrock.cyxbs.mine.R
import com.mredrock.cyxbs.mine.databinding.MineFragmentStampCenterBinding
import com.mredrock.cyxbs.mine.page.stamp.viewModel.StampCenterViewModel
import com.mredrock.cyxbs.mine.util.getDateDay
import com.mredrock.cyxbs.mine.util.ui.BaseDataBindingFragment
import com.mredrock.cyxbs.mine.util.ui.StampTabPageAdapter


class StampCenterFragment :
    BaseDataBindingFragment<MineFragmentStampCenterBinding>(R.layout.mine_fragment_stamp_center) {

    //进入这个fragment时的时间戳
    private var mCurTimeStamp = 0L

    //是否应该展示小蓝点
    private var mShouldShowBlueDot = true

    val viewModel: StampCenterViewModel by activityViewModels()

    //viewpager2的fragment集合
    private var fragmentList: ArrayList<Fragment> = ArrayList()

    //对右上角的图形的宽度进行一个初始化的记录
    private var baseIconWidth = 0


    override fun initView() {
        //进来fragment后就对当前时间戳赋值
        mCurTimeStamp = System.currentTimeMillis()
        //拿到上一次的时间戳 如果是第一次进入 则返回0
        val prefs = activity?.getSharedPreferences("TimeStamp", Context.MODE_PRIVATE)
        val mLastTimeStamp = prefs?.getLong("TimeStamp", 0L)
        //储存当前时间戳
        val editor = activity?.getSharedPreferences("TimeStamp", Context.MODE_PRIVATE)?.edit()
        editor?.putLong("TimeStamp", mCurTimeStamp)
        editor?.apply()

        if (mLastTimeStamp == 0L) {
            //说明是第一次进入，初始化了sp
            mShouldShowBlueDot = true
        } else {
            if (mLastTimeStamp != null) {
                if ((mCurTimeStamp.getDateDay() - mLastTimeStamp.getDateDay()) == 0) {
                    Log.d(TAG, "当前是在同一天")
                    //说明目前在同一天
                    mShouldShowBlueDot = false
                }

            }
        }

        if (fragmentList.size == 0) {
            fragmentList.add(StampTabGoodFragment())
            fragmentList.add(StampTabTaskFragment())
        }

        val stampTabPageAdapter = StampTabPageAdapter(this, fragmentList)
        mBinding.mineStampCenterTlVp.adapter = stampTabPageAdapter
        //设置tabLayout
        mBinding.mineStampCenterTl.let { tabLayout ->
            val customView =
                tabLayout.newTab().setCustomView(R.layout.mine_item_tablayout_stamp_hint).customView
            val hintIv: ImageView? = customView?.findViewById(R.id.mine_item_tl_hint_iv)
            val hintTv: TextView? = customView?.findViewById(R.id.mine_item_tl_hint_tv)
            TabLayoutMediator(
                tabLayout,
                mBinding.mineStampCenterTlVp
            )
            { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "邮票小店"
                    }
                    1 -> {
                        tab.customView = customView
                    }
                }
            }.attach()

            if (!mShouldShowBlueDot) {
                hintIv?.visibility = GONE
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab?.position == 1) {
                        hintIv?.visibility = GONE
                        context?.let {
                            ContextCompat.getColor(
                                it,
                                R.color.common_level_two_font_color
                            )
                        }?.let {
                            hintTv?.setTextColor(it)
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    if (tab?.position == 1) {
                        context?.let {
                            ContextCompat.getColor(
                                it,
                                R.color.common_alpha_level_two_font_color
                            )
                        }?.let {
                            hintTv?.setTextColor(it)
                        }
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    context?.let {
                        ContextCompat.getColor(
                            it,
                            R.color.common_alpha_level_two_font_color
                        )
                    }?.let {
                        hintTv?.setTextColor(it)
                    }
                }

            })
        }
    }

    override fun initData() {
        //对上方的动画进行监听
        mBinding.mineStampCenterAbl.apply {
            addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                Log.d(TAG, "滑动 $verticalOffset 滑动最大值${mBinding.mineStampCenterFl.height}")
                mBinding.mineStampCenterFl.apply {
                    changeAnim(this, this.height + verticalOffset, this.height)
                }
            })
        }
    }


    override fun initOther() {
        mBinding.mineStampCenterFl.setOnClickListener { viewModel.onClickForToDetailPager() }
        mBinding.mineRlStampCenterBack.onClick { activity?.onBackPressed() }
    }

    /**
     * 上方动画改变
     * @param view View 改变的view
     * @param offset Int 偏移值
     * @param maxHeight Int 高度
     */
    private fun changeAnim(view: View, offset: Int, maxHeight: Int) {

        //根据滑动值和高度来计算缩小和透明度

        val ratio = (offset.toFloat() / maxHeight)
        view.scaleX = ratio
        view.scaleY = ratio * ratio * ratio
        view.alpha = ratio




        Log.d(TAG, "offset $offset maxHeight $maxHeight ratio $ratio")
        //右上角动画
        //这里的if主要是进行边界值判断，发现width为0的时候就会填充边界
        if (ratio != 0f && baseIconWidth == 0 && offset != maxHeight) {
            baseIconWidth = mBinding.mineStampCenterIconFl.width
        }
        val layoutParams = mBinding.mineStampCenterIconFl.layoutParams
        mBinding.mineStampCenterIconFl.width.let {
            if (baseIconWidth == 0 || offset == maxHeight) {
                mBinding.mineStampCenterIconFl.visibility = GONE
            } else {
                mBinding.mineStampCenterIconFl.visibility = VISIBLE
                if ((baseIconWidth * (1 - ratio)).toInt() != 0) {
                    layoutParams.width = (baseIconWidth * (1 - ratio)).toInt()
                }
            }
        }

        mBinding.mineStampCenterIconFl.layoutParams = layoutParams

    }

}