package com.mobiai.app.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.mobiai.databinding.DialogOutTop10Binding

class OutTopDialog(context : Context) : Dialog(context) {
    val binding  = DialogOutTop10Binding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnClose.setOnClickListener {
            checkHide()
        }
    }
    private fun checkHide(){
        if(isShowing) {
            hide()
            dismiss()
        }
    }
}