package com.mobiai.app.utils
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.mobiai.base.basecode.ultility.RxBus

class RecordingAudio {

    private var speechRecognizer: SpeechRecognizer? = null
    private var message = arrayListOf<String>()
    fun startSpeechRecognition(context: Context) {
        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN") // Đặt ngôn ngữ là tiếng Việt hoặc ngôn ngữ mong muốn
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {}
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val recognizedText = matches[0]
                    RxBus.publish(IsRecordHaveText(recognizedText))
                    Log.d("TAG", "onResults===>: $recognizedText")
                    // Ở đây bạn có thể xử lý văn bản đã nhận dạng (recognizedText)
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {
            }
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speechRecognizer?.startListening(recognizerIntent)
    }

    fun stopSpeechRecognition() {
        speechRecognizer?.apply {
            stopListening()
            destroy()
        }
        speechRecognizer = null // Giải phóng bộ nhớ khi dừng
    }
}
