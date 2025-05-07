package com.mobiai.base.basecode.ui.activity.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.util.Log
import androidx.viewbinding.ViewBinding
import com.mobiai.base.basecode.ui.activity.BaseActivity

abstract class BaseSplashActivity<V : ViewBinding> : BaseActivity<V>() {
    companion object {
        const val WAKE_LOCK_SPLASH = "wake_lock_splash"
    }

    var isOnStop = false
    private var showNextScreenHandler = Handler(Looper.getMainLooper())

    private var screenLock: PowerManager.WakeLock? = null

    abstract fun openNextScreen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wakeUpScreen()
    }

    @SuppressLint("InvalidWakeLockTag")
    protected fun wakeUpScreen() {
        screenLock = (getSystemService(POWER_SERVICE) as PowerManager).newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            WAKE_LOCK_SPLASH
        )
        screenLock?.acquire(3 * 60 * 1000L)
    }

    override fun onStop() {
        super.onStop()
        isOnStop = true
    }

    override fun onStart() {
        super.onStart()
        isOnStop = false
    }

    override fun onDestroy() {
        super.onDestroy()
        showNextScreenHandler.removeCallbacksAndMessages(null)
    }

    private fun showNextScreen() {
        showNextScreenHandler.postDelayed({
            openNextScreen()
        }, 2000)
    }
}