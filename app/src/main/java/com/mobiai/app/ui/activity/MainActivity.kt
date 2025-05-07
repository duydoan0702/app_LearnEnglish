package com.mobiai.app.ui.activity

import android.content.Context
import android.content.Intent
import com.mobiai.R
import com.mobiai.app.ui.fragment.BottomNavigationFragment
import com.mobiai.app.ui.fragment.SignInFragment
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.activity.BaseActivity
import com.mobiai.databinding.ActivityMainBinding
import java.util.Date

class MainActivity : BaseActivity<ActivityMainBinding>() {
    companion object{
        fun startMain(context: Context, clearTask : Boolean ){
            val intent = Intent(context, MainActivity::class.java).apply {
                if(clearTask){
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            context.startActivity(intent)
        }

    }
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_main
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun createView() {
        if (!isFinishing){
            attachFragment()
        }
    }

    private fun attachFragment(){
        if (SharedPreferenceUtils.lastTimeOpen==0L){
            SharedPreferenceUtils.lastTimeOpen = Date().time
        }
        if (SharedPreferenceUtils.keyUserLogin!=null && SharedPreferenceUtils.emailLogin!=null){
            addFragment(BottomNavigationFragment.instance())
        }
        else{
            replaceFragment(SignInFragment.instance())
        }
    }
}