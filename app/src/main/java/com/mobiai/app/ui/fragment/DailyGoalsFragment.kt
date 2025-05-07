package com.mobiai.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.R
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentDailyGoalsBinding

class DailyGoalsFragment : BaseFragment<FragmentDailyGoalsBinding>() {

    companion object {
        val ITEM_SELECT = "ITEM_SELECT"
        fun instance(itSelect: Int): DailyGoalsFragment {
            Bundle().apply {
                putInt(ITEM_SELECT, itSelect)
                return newInstance(DailyGoalsFragment::class.java, this)
            }
        }
    }

    private var lastSelectedIndex = -1
    override fun initView() {
        arguments?.let {
            lastSelectedIndex = it.getInt(ITEM_SELECT)
            lastSelectedIndex /= 10
        }

        when (lastSelectedIndex) {
            1 ->
                binding.item1.setBackgroundResource(R.drawable.bg_daily_goals_selected)

            2 ->
                binding.item2.setBackgroundResource(R.drawable.bg_daily_goals_selected)

            3 ->
                binding.item3.setBackgroundResource(R.drawable.bg_daily_goals_selected)

            4 ->
                binding.item4.setBackgroundResource(R.drawable.bg_daily_goals_selected)

            5 ->
                binding.item5.setBackgroundResource(R.drawable.bg_daily_goals_selected)
        }

        binding.apply {
            val items = arrayOf(item1, item2, item3, item4, item5)
            for (i in items.indices) {
                items[i].setOnClickListener {
                    for (j in items.indices) {
                        items[j].setBackgroundResource(if (i == j) R.drawable.bg_daily_goals_selected else R.drawable.bg_daily_goals)
                    }
                    lastSelectedIndex = i
                }
            }
        }

        binding.icBack.setOnClickListener {
            handlerBackPressed()
        }
        binding.btnSave.setOnClickListener {
            SharedPreferenceUtils.targetDaily = (lastSelectedIndex + 1) * 10
            handlerBackPressed()
        }
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDailyGoalsBinding {
        return FragmentDailyGoalsBinding.inflate(inflater, container, false)
    }
}