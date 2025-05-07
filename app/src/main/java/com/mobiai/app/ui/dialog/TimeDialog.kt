package com.mobiai.app.ui.dialog


import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.databinding.DialogTimeBinding


class TimeDialog(context: Context, private val listener : OnClickBottomSheetListener) : BottomSheetDialog(context) {
    val binding = DialogTimeBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.pickHour.value = SharedPreferenceUtils.hourAlarm
        binding.pickMinute.value = SharedPreferenceUtils.minuteAlarm



        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            SharedPreferenceUtils.hourAlarm = binding.pickHour.value
            SharedPreferenceUtils.minuteAlarm = binding.pickMinute.value
            listener.onClickSaveFrom()
            checkHideBottomSheet()
        }
    }


    fun checkShowBottomSheet(){
        if (!isShowing) {
            show()
        }
    }
    private fun checkHideBottomSheet(){
        if (isShowing) {
            dismiss()
        }
    }
    interface OnClickBottomSheetListener{
        fun onClickSaveFrom()
    }

}