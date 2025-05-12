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
            listPronunciation.add(Pronunciation("i:", "Sheep", "sheep"))
            listPronunciation.add(Pronunciation("ɪ", "Ship", "ship"))
            listPronunciation.add(Pronunciation("ʊ", "Good", "good"))
            listPronunciation.add(Pronunciation("u:", "Shoot", "shoot"))
            listPronunciation.add(Pronunciation("ɪə", "Here", "here"))
            listPronunciation.add(Pronunciation("eɪ", "Wait", "wait"))
            listPronunciation.add(Pronunciation("e", "Bed", "bed"))
            listPronunciation.add(Pronunciation("ə", "Teacher", "teacher"))
            listPronunciation.add(Pronunciation("ɜ:", "Bird", "bird"))
            listPronunciation.add(Pronunciation("ɔ:", "Door", "door"))
            listPronunciation.add(Pronunciation("ʊə", "Tourist", "tourist"))
            listPronunciation.add(Pronunciation("ɔɪ", "Boy", "boy"))
            listPronunciation.add(Pronunciation("əʊ", "Show", "show"))
            listPronunciation.add(Pronunciation("æ", "Cat", "cat"))
            listPronunciation.add(Pronunciation("ʌ", "Up", "up"))
            listPronunciation.add(Pronunciation("ɑ:", "Far", "far"))
            listPronunciation.add(Pronunciation("ɒ", "On", "on"))
            listPronunciation.add(Pronunciation("eə", "Hair", "hair"))
            listPronunciation.add(Pronunciation("aɪ", "My", "my"))
            listPronunciation.add(Pronunciation("aʊ", "Cow", "cow"))

            listPronunciation.add(Pronunciation("p", "Pea", "pea"))
            listPronunciation.add(Pronunciation("b", "Boat", "boat"))
            listPronunciation.add(Pronunciation("t", "Tea", "tea"))
            listPronunciation.add(Pronunciation("d", "Dog", "dog"))
            listPronunciation.add(Pronunciation("tʃ", "Cheese", "cheese"))
            listPronunciation.add(Pronunciation("dʒ", "June", "june"))
            listPronunciation.add(Pronunciation("k", "Car", "car"))
            listPronunciation.add(Pronunciation("g", "Go", "go"))
            listPronunciation.add(Pronunciation("f", "Fly", "fly"))
            listPronunciation.add(Pronunciation("v", "Video", "video"))
            listPronunciation.add(Pronunciation("θ", "Think", "think"))
            listPronunciation.add(Pronunciation("ð", "This", "this"))
            listPronunciation.add(Pronunciation("s", "See", "see"))
            listPronunciation.add(Pronunciation("z", "Zoo", "zoo"))
            listPronunciation.add(Pronunciation("ʃ", "Shall", "shall"))
            listPronunciation.add(Pronunciation("ʒ", "Television", "television"))
            listPronunciation.add(Pronunciation("m", "Man", "man"))
            listPronunciation.add(Pronunciation("n", "Now", "now"))
            listPronunciation.add(Pronunciation("ŋ", "Sing", "sing"))
            listPronunciation.add(Pronunciation("h", "Hat", "hat"))
            listPronunciation.add(Pronunciation("l", "Love", "love"))
            listPronunciation.add(Pronunciation("r", "Red", "red"))
            listPronunciation.add(Pronunciation("w", "Wet", "wet"))
            listPronunciation.add(Pronunciation("j", "Yes", "yes"))

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