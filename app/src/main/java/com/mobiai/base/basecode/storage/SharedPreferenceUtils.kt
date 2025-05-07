package com.mobiai.base.basecode.storage

import com.mobiai.app.App

object SharedPreferenceUtils {
    private const val FIRST_OPEN_APP = "FIRST_OPEN_APP"
    private const val LANGUAGE = "LANGUAGE"
    private const val EMAIL = "EMAIL"
    private const val KEY_USER = "KEY_USER"
    private const val ALARM = "ALARM"
    private const val HOUR_ALARM = "HOUR_ALARM"
    private const val MINUTE_ALARM = "MINUTE_ALARM"
    private const val MONDAY_ALARM = "MONDAY_ALARM"
    private const val TUESDAY_ALARM = "TUESDAY_ALARM"
    private const val WEDNESDAY_ALARM = "WEDNESDAY_ALARM"
    private const val THURSDAY_ALARM = "THURSDAY_ALARM"
    private const val FRIDAY_ALARM = "FRIDAY_ALARM"
    private const val SATURDAY_ALARM = "SATURDAY_ALARM"
    private const val SUNDAY_ALARM = "SUNDAY_ALARM"
    private const val LESSON_CODE = "LESSON_CODE"
    private const val TARGET_DAILY = "TARGET_DAILY"
    private const val TARGET_DAILY_COUNT = "TARGET_DAILY_COUNT"
    private const val TIME_LAST_DATE = "TIME_LAST_DATE"

    var lastTimeOpen: Long
        get() =
            App.instanceSharePreference.getLongValue(TIME_LAST_DATE, 0)
        set(value) = App.instanceSharePreference.setLongValue(TIME_LAST_DATE, value)


    var keyUserLogin: String?
        get() =
            App.instanceSharePreference.getValue(KEY_USER, null)
        set(value) = App.instanceSharePreference.setValue(KEY_USER, value)

    var emailLogin: String?
        get() =
            //if(BuildConfig.DEBUG) "quan1@gmail.com" else
//            if(BuildConfig.DEBUG) "quan1@gmail.com" else
                App.instanceSharePreference.getValue(EMAIL, null)
        set(value) = App.instanceSharePreference.setValue(EMAIL, value)

    var firstOpenApp: Boolean
        get() = App.instanceSharePreference.getValueBool(FIRST_OPEN_APP, false)
        set(value) = App.instanceSharePreference.setValueBool(FIRST_OPEN_APP, value)
    var targetDaily: Int
        get() = App.instanceSharePreference.getIntValue(TARGET_DAILY)
        set(value) = App.instanceSharePreference.setIntValue(TARGET_DAILY, value)

    var targetDailyCount: Int
        get() = App.instanceSharePreference.getIntValue(TARGET_DAILY_COUNT)
        set(value) = App.instanceSharePreference.setIntValue(TARGET_DAILY_COUNT, value)

    var languageCode: String?
        get() = App.instanceSharePreference.getValue(LANGUAGE, null)
        set(value) = App.instanceSharePreference.setValue(LANGUAGE, value)
    var alarm: Boolean
        get() = App.instanceSharePreference.getValueBool(ALARM, false)
        set(value) = App.instanceSharePreference.setValueBool(ALARM, value)
    var hourAlarm: Int
        get() = App.instanceSharePreference.getIntValue(HOUR_ALARM, 8)
        set(value) = App.instanceSharePreference.setIntValue(HOUR_ALARM, value)
    var minuteAlarm: Int
        get() = App.instanceSharePreference.getIntValue(MINUTE_ALARM, 0)
        set(value) = App.instanceSharePreference.setIntValue(MINUTE_ALARM, value)
    var monday: Boolean
        get() = App.instanceSharePreference.getValueBool(MONDAY_ALARM, true)
        set(value) = App.instanceSharePreference.setValueBool(MONDAY_ALARM, value)
    var tuesday: Boolean
        get() = App.instanceSharePreference.getValueBool(TUESDAY_ALARM, false)
        set(value) = App.instanceSharePreference.setValueBool(TUESDAY_ALARM, value)
    var wednesday: Boolean
        get() = App.instanceSharePreference.getValueBool(WEDNESDAY_ALARM, false)
        set(value) = App.instanceSharePreference.setValueBool(WEDNESDAY_ALARM, value)
    var thursday: Boolean
        get() = App.instanceSharePreference.getValueBool(THURSDAY_ALARM, false)
        set(value) = App.instanceSharePreference.setValueBool(THURSDAY_ALARM, value)
    var friday: Boolean
        get() = App.instanceSharePreference.getValueBool(FRIDAY_ALARM, false)
        set(value) = App.instanceSharePreference.setValueBool(FRIDAY_ALARM, value)
    var saturday: Boolean
        get() = App.instanceSharePreference.getValueBool(SATURDAY_ALARM, false)
        set(value) = App.instanceSharePreference.setValueBool(SATURDAY_ALARM, value)
    var sunday: Boolean
        get() = App.instanceSharePreference.getValueBool(SUNDAY_ALARM, false)
        set(value) = App.instanceSharePreference.setValueBool(SUNDAY_ALARM, value)
    var lessonCode: String?
        get() = App.instanceSharePreference.getValue(LESSON_CODE, null)
        set(value) = App.instanceSharePreference.setValue(LESSON_CODE, value)
}