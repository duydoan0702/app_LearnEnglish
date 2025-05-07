package com.mobiai.app.ui.activity

import android.os.Looper
import com.mobiai.R
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.activity.BaseActivity
import com.mobiai.databinding.ActivitySplashBinding

class SplashActivityNew : BaseActivity<ActivitySplashBinding>() {
    override fun getLayoutResourceId(): Int {
        return R.layout.activity_splash
    }

    override fun getViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun createView() {
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            if (SharedPreferenceUtils.languageCode == null) {
                LanguageActivity.start(this, true)
            } else {
                MainActivity.startMain(this, true)
            }
        }, 3000)
    }
}