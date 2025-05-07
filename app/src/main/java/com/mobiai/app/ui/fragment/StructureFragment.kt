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
import com.mobiai.app.adapter.StructureAdapter
import com.mobiai.app.model.Vocabulary
import com.mobiai.app.utils.makeGone
import com.mobiai.app.utils.visible
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentVocabularyStructureBinding

class StructureFragment : BaseFragment<FragmentVocabularyStructureBinding>() {
    companion object {
        fun instance(): StructureFragment {
            return newInstance(StructureFragment::class.java)
        }
    }

    private lateinit var structureAdapter: StructureAdapter
    private var listStructure: ArrayList<Vocabulary.Sentence_Structure> = arrayListOf()
    override fun initView() {
        initAdapter()
        initData()
    }

    private fun initAdapter() {
        structureAdapter = StructureAdapter(requireContext())
        binding.rcvStructure.adapter = structureAdapter
    }

    private fun initData() {
        binding.frLoading.visible()
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.Sentence_Structure)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val structure = userSnapshot.getValue(Vocabulary.Sentence_Structure::class.java)
                    if (structure != null) {
                        val structureNew = Vocabulary.Sentence_Structure(
                            structure.nameStructure,
                            structure.structure,
                            structure.explain,
                            structure.example
                        )
                        listStructure.add(structureNew)
                    }
                }
                listStructure.sortBy { it.nameStructure }
                structureAdapter.setItems(listStructure)
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
    ): FragmentVocabularyStructureBinding {
        return FragmentVocabularyStructureBinding.inflate(inflater, container, false)
    }
}