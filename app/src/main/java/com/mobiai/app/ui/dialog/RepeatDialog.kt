package com.mobiai.app.ui.dialog


import android.content.Context
import android.os.Bundle
import android.widget.CheckBox
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mobiai.R
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.databinding.DialogRepeatBinding


class RepeatDialog(context: Context, private val listener : OnClickBottomSheetListener) : BottomSheetDialog(context) {
    val binding = DialogRepeatBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getRepeat()
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            setRepeat()
            listener.onClickSaveFrom()
            checkHideBottomSheet()
        }
        binding.cbMon.setOnClickListener{
            changeStatus(binding.cbMon)
            checkChange()
        }
        binding.cbTue.setOnClickListener{
            changeStatus(binding.cbTue)
            checkChange()
        }
        binding.cbWed.setOnClickListener{
            changeStatus(binding.cbWed)
            checkChange()
        }
        binding.cbThus.setOnClickListener{
            changeStatus(binding.cbThus)
            checkChange()
        }
        binding.cbFri.setOnClickListener{
            changeStatus(binding.cbFri)
            checkChange()
        }
        binding.cbSat.setOnClickListener{
            changeStatus(binding.cbSat)
            checkChange()
        }
        binding.cbSun.setOnClickListener{
            changeStatus(binding.cbSun)
            checkChange()
        }
    }
    fun changeStatus(view: CheckBox){
        val typeface = ResourcesCompat.getFont(context, R.font.gilroy_semi_bold)
        val typeface_2 = ResourcesCompat.getFont(context, R.font.roboto_regular)
        if(view.isChecked){
            view.setBackgroundResource(R.drawable.bg_select_day)
            view.setTextColor(context.resources.getColor(R.color.black))
            view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_select_repeat,0,0,0)
            view.typeface = typeface
        }
        if(!view.isChecked){
            view.setBackgroundResource(R.drawable.bg_unselect_day)
            view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_unselect_repeat,0,0,0)
            view.setTextColor(context.resources.getColor(R.color.gray_text))
            view.typeface = typeface_2
        }
    }

    fun checkShowBottomSheet(){
        if (!isShowing) {
            show()
        }
    }
    fun checkHideBottomSheet(){
        if (isShowing) {
            dismiss()
        }
    }
    interface OnClickBottomSheetListener{
        fun onClickSaveFrom()
    }
    fun setRepeat(){
        if(binding.cbMon.isChecked)
            SharedPreferenceUtils.monday = true
        if(!binding.cbMon.isChecked)
            SharedPreferenceUtils.monday = false

        if(binding.cbTue.isChecked)
            SharedPreferenceUtils.tuesday = true
        if(!binding.cbTue.isChecked)
            SharedPreferenceUtils.tuesday = false

        if(binding.cbWed.isChecked)
            SharedPreferenceUtils.wednesday = true
        if(!binding.cbWed.isChecked)
            SharedPreferenceUtils.wednesday = false

        if(binding.cbThus.isChecked)
            SharedPreferenceUtils.thursday = true
        if(!binding.cbThus.isChecked)
            SharedPreferenceUtils.thursday = false

        if(binding.cbFri.isChecked)
            SharedPreferenceUtils.friday = true
        if(!binding.cbFri.isChecked)
            SharedPreferenceUtils.friday = false

        if(binding.cbSat.isChecked)
            SharedPreferenceUtils.saturday = true
        if(!binding.cbSat.isChecked)
            SharedPreferenceUtils.saturday = false

        if(binding.cbSun.isChecked)
            SharedPreferenceUtils.sunday = true
        if(!binding.cbSun.isChecked)
            SharedPreferenceUtils.sunday = false
        checkChange()
    }
    fun getRepeat() {
        if (SharedPreferenceUtils.monday){
            binding.cbMon.isChecked = true
            changeStatus(binding.cbMon)
        }
        if (!SharedPreferenceUtils.monday) {
            binding.cbMon.isChecked = false
            changeStatus(binding.cbMon)
        }

        if (SharedPreferenceUtils.tuesday) {
            binding.cbTue.isChecked = true
            changeStatus(binding.cbTue)
        }
        if (!SharedPreferenceUtils.tuesday) {
            binding.cbTue.isChecked = false
            changeStatus(binding.cbTue)
        }

        if (SharedPreferenceUtils.wednesday) {
            binding.cbWed.isChecked = true
            changeStatus(binding.cbWed)
        }
        if (!SharedPreferenceUtils.wednesday) {
            binding.cbWed.isChecked = false
            changeStatus(binding.cbWed)
        }

        if (SharedPreferenceUtils.thursday) {
            binding.cbThus.isChecked = true
            changeStatus(binding.cbThus)
        }
        if (!SharedPreferenceUtils.thursday) {
            binding.cbThus.isChecked = false
            changeStatus(binding.cbThus)
        }

        if (SharedPreferenceUtils.friday) {
            binding.cbFri.isChecked = true
            changeStatus(binding.cbFri)
        }
        if (!SharedPreferenceUtils.friday) {
            binding.cbFri.isChecked = false
            changeStatus(binding.cbFri)
        }

        if (SharedPreferenceUtils.saturday) {
            binding.cbSat.isChecked = true
            changeStatus(binding.cbSat)
        }
        if (!SharedPreferenceUtils.saturday) {
            binding.cbSat.isChecked = false
            changeStatus(binding.cbSat)
        }


        if (SharedPreferenceUtils.sunday) {
            binding.cbSun.isChecked = true
            changeStatus(binding.cbSun)
        }
        if (!SharedPreferenceUtils.sunday) {
            binding.cbSun.isChecked = false
            changeStatus(binding.cbSun)
        }
    }
    fun checkChange(){
        if(!binding.cbMon.isChecked && !binding.cbTue.isChecked  && !binding.cbWed.isChecked  && !binding.cbThus.isChecked  && !binding.cbFri.isChecked  && !binding.cbSat.isChecked  && !binding.cbSun.isChecked )
            binding.cbMon.isChecked  = true
        changeStatus(binding.cbMon)
    }
}