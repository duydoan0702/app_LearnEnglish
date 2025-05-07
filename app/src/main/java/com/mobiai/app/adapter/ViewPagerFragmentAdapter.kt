package com.mobiai.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mobiai.app.ui.fragment.ForgotPasswordFragment
import com.mobiai.app.ui.fragment.StructureFragment
import com.mobiai.app.ui.fragment.WordTypeFragment


class ViewPagerFragmentAdapter(owner : FragmentActivity, val numberPage : Int)
    : FragmentStateAdapter(owner) {
    override fun getItemCount(): Int {
        return numberPage
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StructureFragment.instance()
            else -> WordTypeFragment.instance()
        }

    }
}