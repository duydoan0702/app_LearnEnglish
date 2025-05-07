package com.mobiai.app.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.databinding.DialogSuccessReceiverGiftBinding
import com.mobiai.databinding.DialogTimeBinding

class SuccessGiftDialog(context: Context) : Dialog(context) {
    val binding = DialogSuccessReceiverGiftBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnClose.setOnClickListener {
            checkHide()
        }
    }
    private fun checkHide(){
        if (isShowing) {
            dismiss()
        }
    }
}