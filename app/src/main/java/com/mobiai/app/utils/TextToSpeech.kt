package com.mobiai.app.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.BatteryManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import android.widget.Toast
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import java.util.Locale

class Announcer(context: Context) : OnInitListener {
    var context = context
    var tts: TextToSpeech? = null
//    private var countryLanguage = Locale(SharedPreferenceUtils.languageCode).country.toUpperCase()
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale("vi","VN"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                context.showToast("Language not supported")
            }
        }
    }

    fun initTTS(context: Context) {
        tts = TextToSpeech(context, this)

    }

    fun setSpeechRate(value:Int){
        if (value==0){
            tts?.setSpeechRate(0.1f)
        }
        else{
            tts?.setSpeechRate(value / 40f)
        }
    }
    fun readText(text: String) {
        val audioManager =
            context.applicationContext?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC),
            0
        )
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "read")

    }

    fun openTextToSpeechSettings(context: Context) {
        val intent = Intent()
        intent.action = "com.android.settings.TTS_SETTINGS"
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Cần cài đặt ngôn ngữ nói !", Toast.LENGTH_LONG).show()
        }
    }
}

