package com.mredrock.cyxbs.mine.page.stamp.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mredrock.cyxbs.mine.R
import com.mredrock.cyxbs.mine.databinding.MineFragmentStampDetailItemBinding
import com.mredrock.cyxbs.mine.page.stamp.adapter.StampDetailRvAdapter
import com.mredrock.cyxbs.mine.util.ui.BaseDataBindingFragment
import kotlinx.android.synthetic.main.mine_fragment_stamp_detail_item.*

/**
 * Author by OkAndGreat，Date on 2021/8/4.
 * 邮票明细页面viewpager的list fragment
 *
 */
class StampDetailListFragment :
    BaseDataBindingFragment<MineFragmentStampDetailItemBinding>(R.layout.mine_fragment_stamp_detail_item) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mine_rv_stamp_detail.layoutManager = LinearLayoutManager(activity)
        mine_rv_stamp_detail.adapter = StampDetailRvAdapter()
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initOther() {

    }

}