package com.mobiai.app.ui.fragment

import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.App
import com.mobiai.app.adapter.WordTypeAdapter
import com.mobiai.app.model.Vocabulary
import com.mobiai.app.utils.makeGone
import com.mobiai.app.utils.visible
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentVocabularyWordTypeBinding

class WordTypeFragment : BaseFragment<FragmentVocabularyWordTypeBinding>() {
    companion object {
        fun instance(): WordTypeFragment {
            return newInstance(WordTypeFragment::class.java)
        }
    }

    private lateinit var wordTypeAdapter: WordTypeAdapter
    private var listWordType: ArrayList<Vocabulary.Word_Types> = arrayListOf()
    override fun initView() {
        initAdapter()
        initData()
    }

    private fun initAdapter() {
        wordTypeAdapter = WordTypeAdapter(requireContext())
        binding.rcvWordType.adapter = wordTypeAdapter
    }

    private fun initData() {
        binding.frLoading.visible()
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.Word_Types)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val structure = userSnapshot.getValue(Vocabulary.Word_Types::class.java)
                    if (structure != null) {
                        val structureNew = Vocabulary.Word_Types(
                            structure.nameWord,
                            structure.describe,
                            structure.example
                        )
                        listWordType.add(structureNew)
                    }
                }
                listWordType.sortBy { it.nameWord }
                wordTypeAdapter.setItems(listWordType)
                Handler().postDelayed({
                    binding.frLoading.makeGone()
                }, 100)

            }

            override fun onCancelled(error: DatabaseError) {
                Handler().postDelayed({
                    binding.frLoading.makeGone()
                }, 100)
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentVocabularyWordTypeBinding {
        return FragmentVocabularyWordTypeBinding.inflate(inflater, container, false)
    }
}