package com.mobiai.app.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mobiai.R
import com.mobiai.app.App
import com.mobiai.app.model.AnsweredQuestions
import com.mobiai.app.model.Question
import com.mobiai.app.model.Results
import com.mobiai.base.basecode.adapter.BaseAdapter
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.databinding.ItemQuestionResultBinding
import com.mobiai.databinding.ItemResultsBinding

class QuestionResultAdapter(val context : Context, val listener : OnQuestionResultClickListener) : BaseAdapter<AnsweredQuestions, ItemQuestionResultBinding>() {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemQuestionResultBinding {
        return ItemQuestionResultBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: ItemQuestionResultBinding, item: AnsweredQuestions, position: Int) {
        queryContent(item.codeQuestion,binding.txtContent,binding.txtSelectA,binding.txtSelectB,binding.txtSelectC,binding.txtSelectD,item.option)
//        binding.txtSelectA.text = item.option

        binding.root.setOnClickListener {
            listener.onClickItemListener(item)
        }
    }

    private fun queryContent(questionCode:String,view: TextView,view1: TextView,view2: TextView,view3: TextView,view4: TextView,seleted:String){
        val db = FirebaseDatabase.getInstance()
        val ref = db.getReference(App.QUESTION)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val results = userSnapshot.getValue(Question::class.java)
                    if (results != null) {
                        if (results.questionCode == questionCode){
                            Log.d("VuLT", "onDataChange: $results")
                            view.text = results.content
                            view1.text = results.option1
                            view2.text = results.option2
                            view3.text = results.option3
                            view4.text = results.option4
                            isCheckAnswer(view1,results.answer,seleted)
                            isCheckAnswer(view2,results.answer,seleted)
                            isCheckAnswer(view3,results.answer,seleted)
                            isCheckAnswer(view4,results.answer,seleted)
//                            break
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                context.showToast(error.message)
            }
        })
    }

    private fun isCheckAnswer(view: TextView,results:String,seleted:String){
        if (seleted == results){
            view.background = if (view.text.toString().trim() == seleted)
                context.resources.getDrawable(R.drawable.bg_selected)
            else context.resources.getDrawable(R.drawable.bg_un_selected)
        }
        else{
            view.background = if (view.text.toString().trim() == seleted)
                context.resources.getDrawable(R.drawable.bg_error_selected)
            else context.resources.getDrawable(R.drawable.bg_un_selected)
        }

    }
    interface OnQuestionResultClickListener {
        fun onClickItemListener(answeredQuestions: AnsweredQuestions)
    }
}