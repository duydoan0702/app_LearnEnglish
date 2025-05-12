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
            listPronunciation.add(Pronunciation("A", "Apple", "a"))
            listPronunciation.add(Pronunciation("B", "Ball", "b"))
            listPronunciation.add(Pronunciation("C", "Cat", "c"))
            listPronunciation.add(Pronunciation("D", "Dog", "d"))
            listPronunciation.add(Pronunciation("E", "Elephant", "e"))
            listPronunciation.add(Pronunciation("F", "Fish", "f"))
            listPronunciation.add(Pronunciation("G", "Goat", "g"))
            listPronunciation.add(Pronunciation("H", "Hat", "h"))
            listPronunciation.add(Pronunciation("I", "Ice", "i"))
            listPronunciation.add(Pronunciation("J", "Juice", "j"))
            listPronunciation.add(Pronunciation("K", "Kite", "k"))
            listPronunciation.add(Pronunciation("L", "Lion", "l"))
            listPronunciation.add(Pronunciation("M", "Monkey", "m"))
            listPronunciation.add(Pronunciation("N", "Nest", "n"))
            listPronunciation.add(Pronunciation("O", "Orange", "o"))
            listPronunciation.add(Pronunciation("P", "Pen", "p"))
            listPronunciation.add(Pronunciation("Q", "Queen", "q"))
            listPronunciation.add(Pronunciation("R", "Rabbit", "r"))
            listPronunciation.add(Pronunciation("S", "Sun", "s"))
            listPronunciation.add(Pronunciation("T", "Tiger", "t"))
            listPronunciation.add(Pronunciation("U", "Umbrella", "u"))
            listPronunciation.add(Pronunciation("V", "Violin", "v"))
            listPronunciation.add(Pronunciation("W", "Water", "w"))
            listPronunciation.add(Pronunciation("X", "Xylophone", "x"))
            listPronunciation.add(Pronunciation("Y", "Yellow", "y"))
            listPronunciation.add(Pronunciation("Z", "Zebra", "z"))
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