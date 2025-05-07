package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mobiai.R
import com.mobiai.app.adapter.ViewPagerFragmentAdapter
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentVocabularyBinding

class VocabularyFragment : BaseFragment<FragmentVocabularyBinding>() {
    companion object {
        fun instance(): VocabularyFragment {
            return newInstance(VocabularyFragment::class.java)
        }
    }

    private val listTitle: ArrayList<String> = arrayListOf()
    private lateinit var viewPagerFragmentAdapter: ViewPagerFragmentAdapter

    override fun initView() {
        initViewPager()
        binding.ivBack.setOnClickListener {
            handlerBackPressed()
        }
    }

    private fun initViewPager() {
        listTitle.add(getString(R.string._structure))
        listTitle.add(getString(R.string.word_type))
        viewPagerFragmentAdapter = ViewPagerFragmentAdapter(requireActivity(), listTitle.size)
        binding.viewPager2.adapter = viewPagerFragmentAdapter
        binding.viewPager2.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        binding.viewPager2.setCurrentItem(0, false)
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, postition ->
            tab.text = listTitle[postition]
        }.attach()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> binding.viewPager2.setCurrentItem(0, false)
                    1 -> binding.viewPager2.setCurrentItem(1, false)
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {

                }
            }
        })
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentVocabularyBinding {
        return FragmentVocabularyBinding.inflate(inflater, container, false)
    }
}