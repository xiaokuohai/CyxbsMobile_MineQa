package com.mredrock.cyxbs.mine.util.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mredrock.cyxbs.mine.page.stamp.adapter.GetChangeDiffUtil

/**
 * @author xiao
 * @param M 这个是viewModel里面的List类的泛型类
 * @param DB: ViewDataBinding
 * @property layoutId Int  item的资源文件
 * @property items LiveData<List<M>> //list文件，每次添加会更新
 * @property lifecycleOwner LifecycleOwner //应对多个fragment的生命周期管理
 *
 */
abstract class BaseDataBindingAdapter<M,DB: ViewDataBinding>
    (
    @LayoutRes private val layoutId:Int,
    private val items : LiveData<List<M>>,
    private val lifecycleOwner: LifecycleOwner
    ):
    RecyclerView.Adapter<BaseDataBindingAdapter.BaseDataBindingViewHolder>() {
    private var mItems = items.value
    private var firstInto = true
    init {
        items.observe(lifecycleOwner, Observer{ newList ->
            if (firstInto) {
                notifyDataSetChanged()
                firstInto = false
            }
            mItems?.let {
                DiffUtil.calculateDiff(GetChangeDiffUtil(it,newList)).dispatchUpdatesTo(this)
            }
        })

    }

    class BaseDataBindingViewHolder(var mbinding: ViewDataBinding): RecyclerView.ViewHolder(mbinding.root) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseDataBindingViewHolder {
        val mBinding: DB = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
        return BaseDataBindingViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: BaseDataBindingViewHolder, position: Int) {

        val mBinding: DB? = DataBindingUtil.getBinding(holder.itemView)
        items.value?.get(position)?.let { this.onBindItem(mBinding, it,position) };
    }

    override fun getItemCount(): Int {
        return  items.value?.size ?: 0
    }

    //mBinding为View，item为数据
    abstract fun onBindItem(mBinding: DB?, item: M,position:Int)

}