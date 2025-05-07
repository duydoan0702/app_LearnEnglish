package com.mobiai.app

import android.app.Application
import com.mobiai.base.basecode.storage.SharedPreferencesManager

class App : Application() {

    companion object {
        val USER = "user"
        val LEVEL = "level"
        val TOPIC = "topic"
        val TOPIC_QUESTION = "topic_question"
        val LESSON = "lesson"
        val QUESTION = "question"
        val ANSWEREDQUESTIONS = "answeredquestions"
        val RESULTS = "results"
        val Sentence_Structure = "Sentence_Structure"
        val Word_Types = "word_type"

        val GIFT = "gift"


        private var instance: App? = null
        fun getInstance(): App? {
            return instance
        }

        lateinit var instanceSharePreference: SharedPreferencesManager

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        instanceSharePreference = SharedPreferencesManager(applicationContext)

    }
}