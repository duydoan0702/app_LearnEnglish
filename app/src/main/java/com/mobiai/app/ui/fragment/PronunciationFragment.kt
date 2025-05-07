package com.mobiai.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.mobiai.app.adapter.PronunciationAdapter
import com.mobiai.app.model.Pronunciation
import com.mobiai.app.utils.Announcer
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentPronunciationBinding

class PronunciationFragment : BaseFragment<FragmentPronunciationBinding>() {
    companion object {
        fun instance(): PronunciationFragment {
            return newInstance(PronunciationFragment::class.java)
        }
    }

    private var pronunciationAdapter: PronunciationAdapter? = null

    override fun initView() {
        initData()
    }

    fun initData() {
        announcer = Announcer(requireActivity())
        announcer?.initTTS(requireActivity())
        pronunciationAdapter = PronunciationAdapter(requireContext())
        val listPronunciation: ArrayList<Pronunciation> = arrayListOf()
        runBackground({
            listPronunciation.add(Pronunciation("Aa", "Na"))
            listPronunciation.add(Pronunciation("Ăă", "Ăn"))
            listPronunciation.add(Pronunciation("Ââ", "Trâu"))
            listPronunciation.add(Pronunciation("Bb", "Bánh", "bờ"))
            listPronunciation.add(Pronunciation("Cc", "Cá cờ", "cờ"))
            listPronunciation.add(Pronunciation("Dd", "Da cá", "dờ"))
            listPronunciation.add(Pronunciation("Đđ", "Đo độ"))
            listPronunciation.add(Pronunciation("Ee", "Em bé"))
            listPronunciation.add(Pronunciation("Êê", "Êm ấm"))
            listPronunciation.add(Pronunciation("Gg", "Ghế"))
            listPronunciation.add(Pronunciation("Hh", "Hoa", "hờ"))
            listPronunciation.add(Pronunciation("Ii", "Bí"))
            listPronunciation.add(Pronunciation("Kk", "Kiến"))
            listPronunciation.add(Pronunciation("Ll", "Lan"))
            listPronunciation.add(Pronunciation("Mm", "Mẹ"))
            listPronunciation.add(Pronunciation("Nn", "No"))
            listPronunciation.add(Pronunciation("Oo", "Ong"))
            listPronunciation.add(Pronunciation("Ôô", "Ông"))
            listPronunciation.add(Pronunciation("Ơơ", "Ơi"))
            listPronunciation.add(Pronunciation("Pp", "Phở", "pờ"))
            listPronunciation.add(Pronunciation("Qq", "Quế", "quờ"))
            listPronunciation.add(Pronunciation("Rr", "Rung"))
            listPronunciation.add(Pronunciation("Ss", "Sang", "Sờ"))
            listPronunciation.add(Pronunciation("Tt", "Tươi", "tờ"))
            listPronunciation.add(Pronunciation("Uu", "Uống"))
            listPronunciation.add(Pronunciation("Ưư", "Mừng"))
            listPronunciation.add(Pronunciation("Vv", "Voi", "vờ"))
            listPronunciation.add(Pronunciation("Xx", "Xôi", "xờ"))
            listPronunciation.add(Pronunciation("Yy", "Yêu"))
        }, {
            pronunciationAdapter?.setItems(listPronunciation)
            binding.recyclerViewPronun.layoutManager = GridLayoutManager(requireContext(), 3)
            // binding.recyclerViewPronun.addItemDecoration(ItemSpacingDecoration(7,8))
            binding.recyclerViewPronun.adapter = pronunciationAdapter
        }, {
            pronunciationAdapter?.setItems(listPronunciation)
            binding.recyclerViewPronun.layoutManager = GridLayoutManager(requireContext(), 3)
            //binding.recyclerViewPronun.addItemDecoration(ItemSpacingDecoration(7,8))
            binding.recyclerViewPronun.adapter = pronunciationAdapter
        })
        binding.btnTest.setOnClickListener {
            replaceFragment(BottomNavigationFragment.instance())
        }

    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPronunciationBinding {
        return FragmentPronunciationBinding.inflate(inflater, container, false)
    }

}

var announcer: Announcer? = null