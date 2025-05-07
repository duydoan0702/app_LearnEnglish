package com.mobiai.app.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.app.App
import com.mobiai.app.adapter.ItemSpacingDecoration
import com.mobiai.app.adapter.QuestionResultAdapter
import com.mobiai.app.model.AnsweredQuestions
import com.mobiai.app.model.Question
import com.mobiai.app.utils.makeGone
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentQuestionResultsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionResultFragment : BaseFragment<FragmentQuestionResultsBinding>() {
    companion object {
        val TITLE = "TITLE"
        val CODE_RESULT = "CODE_RESULT"
        fun instance(title: String, codeResult: String): QuestionResultFragment {
            Bundle().apply {
                putString(TITLE, title)
                putString(CODE_RESULT, codeResult)
                return newInstance(QuestionResultFragment::class.java, this)
            }
        }
    }

    private var codeResult = ""
    private var lessonResultAdapter: QuestionResultAdapter? = null
    val db = FirebaseDatabase.getInstance()
    val ref = db.getReference(App.ANSWEREDQUESTIONS)
    var listQuestionResult = arrayListOf<AnsweredQuestions>()
    override fun initView() {
        arguments?.let {
            binding.txtTitle.text = it.getString(TITLE) ?: "Question Result"
            codeResult = it.getString(CODE_RESULT).toString()
        }
        if (!isAdded) return
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
    ): FragmentQuestionResultsBinding {
        return FragmentQuestionResultsBinding.inflate(inflater, container, false)
    }

    private fun getLessonResults() {
        if (isAdded){
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listQuestionResult.clear()
                    for (userSnapshot in dataSnapshot.children) {
                        val results = userSnapshot.getValue(AnsweredQuestions::class.java)
                        if (results != null && results.codeResult == codeResult) {
                            listQuestionResult.add(results)
                        }
                    }

                    listQuestionResult.sortByDescending { it.codeQuestion }

                    lessonResultAdapter = QuestionResultAdapter(requireContext(),
                        object : QuestionResultAdapter.OnQuestionResultClickListener {
                            override fun onClickItemListener(answeredQuestions: AnsweredQuestions) {
                                //todo
                            }
                        })
                    lessonResultAdapter?.setItems(listQuestionResult)

                    binding.rcvQuestionResults.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    binding.rcvQuestionResults.addItemDecoration(ItemSpacingDecoration(7, 8))
                    binding.rcvQuestionResults.adapter = lessonResultAdapter
                    Handler().postDelayed({
                        binding.frLoading.makeGone()
                    },1000)
                }

                override fun onCancelled(error: DatabaseError) {
                    requireContext().showToast(error.message)
                    binding.frLoading.makeGone()
                }
            })
        }
    }
}