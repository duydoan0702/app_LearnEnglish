package com.mobiai.app.ui.fragment

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.App
import com.mobiai.app.adapter.ItemSpacingDecoration
import com.mobiai.app.adapter.LessonResultAdapter
import com.mobiai.app.model.Results
import com.mobiai.app.utils.makeGone
import com.mobiai.app.utils.makeVisible
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentLessonResultsBinding

class LessonResultFragment : BaseFragment<FragmentLessonResultsBinding>() {
    companion object {
        fun instance(): LessonResultFragment {
            return newInstance(LessonResultFragment::class.java)
        }
    }

    private var lessonResultAdapter: LessonResultAdapter? = null
    val db = FirebaseDatabase.getInstance()
    val ref = db.getReference(App.RESULTS)
    var listLessonResult = arrayListOf<Results>()
    override fun initView() {
        getLessonResults()
        binding.btnClose.setOnClickListener {
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
    ): FragmentLessonResultsBinding {
        return FragmentLessonResultsBinding.inflate(inflater, container, false)
    }

    private fun getLessonResults() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listLessonResult.clear()
                for (userSnapshot in dataSnapshot.children) {
                    val results = userSnapshot.getValue(Results::class.java)
                    if (results != null && results.userName == SharedPreferenceUtils.emailLogin) {
                        listLessonResult.add(results)
                    }
                }
                if (listLessonResult.isEmpty()) {
                    binding.lnEmpty.makeVisible()
                    binding.rcvQuestionResults.makeGone()
                } else {
                    binding.rcvQuestionResults.makeVisible()
                    binding.lnEmpty.makeGone()
                }
                binding.rcvQuestionResults.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.rcvQuestionResults.addItemDecoration(ItemSpacingDecoration(7, 8))
                listLessonResult.sortByDescending { it.numberCorrect }
                lessonResultAdapter = LessonResultAdapter(requireContext(),
                    object : LessonResultAdapter.OnResultClickListener {
                        override fun onClickItemListener(results: Results) {
                            binding.frLoading.makeVisible()
                            addFragment(
                                QuestionResultFragment.instance(
                                    results.lessonCode,
                                    results.codeResult
                                )
                            )
                            Handler().postDelayed({
                                binding.frLoading.makeGone()
                            }, 200)
                        }

                    })
                lessonResultAdapter?.setItems(listLessonResult)
                binding.rcvQuestionResults.adapter = lessonResultAdapter


            }

            override fun onCancelled(error: DatabaseError) {
                requireContext().showToast(error.message)
            }
        })
    }
}