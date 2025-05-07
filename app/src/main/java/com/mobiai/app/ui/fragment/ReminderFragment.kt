package com.mobiai.app.ui.fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.mobiai.R
import com.mobiai.app.broadcast.ReminderReceiver
import com.mobiai.app.ui.dialog.TimeDialog
import com.mobiai.base.basecode.extensions.showToast
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentReminderBinding
import java.util.Calendar

class ReminderFragment : BaseFragment<FragmentReminderBinding>() {
    companion object {
        fun instance(): ReminderFragment {
            return newInstance(ReminderFragment::class.java)
        }
    }

    private var bottomSheetTimeDialog: TimeDialog? = null
    private var dayOfWeek: ArrayList<String> = arrayListOf()
    var intent: Intent? = null
    override fun initView() {
        initData()
        onClick()
    }

    private fun initData() {
        createNotification()
        dayOfWeek = ArrayList()
        checkAutoToggle()
    }

    private fun onClick() {
        if (SharedPreferenceUtils.minuteAlarm >= 10)
            binding.txtTime.text =
                "${SharedPreferenceUtils.hourAlarm}:${SharedPreferenceUtils.minuteAlarm}"
        else
            binding.txtTime.text =
                "${SharedPreferenceUtils.hourAlarm}:0${SharedPreferenceUtils.minuteAlarm}"

        binding.txtTime.setOnClickListener {
            openTimeBottomSheet()
            setAlarm()
        }
        binding.turnOnReminder.setOnClickListener {
            SharedPreferenceUtils.alarm = !SharedPreferenceUtils.alarm
            checkAutoToggle()
            setAlarm()

        }
        binding.btnClose.setOnClickListener {
            handlerBackPressed()
        }
    }

    private fun openTimeBottomSheet() {
        if (bottomSheetTimeDialog == null) {
            bottomSheetTimeDialog = TimeDialog(
                requireContext(),
                object : TimeDialog.OnClickBottomSheetListener {
                    @SuppressLint("SetTextI18n")
                    override fun onClickSaveFrom() {
                        if (SharedPreferenceUtils.minuteAlarm >= 10)
                            binding.txtTime.text =
                                "${SharedPreferenceUtils.hourAlarm}:${SharedPreferenceUtils.minuteAlarm}"
                        else
                            binding.txtTime.text =
                                "${SharedPreferenceUtils.hourAlarm}:0${SharedPreferenceUtils.minuteAlarm}"
                    }

                })
        }
        bottomSheetTimeDialog?.checkShowBottomSheet()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReminderBinding {
        return FragmentReminderBinding.inflate(inflater, container, false)
    }

    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }

    private fun checkAutoToggle() {
        if (SharedPreferenceUtils.alarm) {
            binding.turnOnReminder.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.toggle_turn_on
                )
            )
        } else {
            binding.turnOnReminder.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.toggle_turn_off
                )
            )
        }
    }

    private fun setAlarm() {
        requireContext().showToast(SharedPreferenceUtils.hourAlarm.toString() + SharedPreferenceUtils.minuteAlarm.toString())
        if (SharedPreferenceUtils.alarm) {
            showNotification(SharedPreferenceUtils.hourAlarm, SharedPreferenceUtils.minuteAlarm)
        }
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_ID
            val description = "message reminder"
            val important = NotificationManager.IMPORTANCE_HIGH
            val chanel = NotificationChannel(CHANNEL_ID, name, important)
            chanel.description = description

            val notificationManager =
                requireContext().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(chanel)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun showNotification(setHour: Int, setMinute: Int) {
        // Lấy đối tượng AlarmManager
        val alarmMgr = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), ReminderReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(
                requireContext(),
                15112002,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        // Lấy thời gian hiện tại để so sánh và tính toán thời gian khởi động của hành động
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()

        calendar.set(Calendar.HOUR_OF_DAY, setHour) // Đặt giờ là 9h sáng
        calendar.set(Calendar.MINUTE, setMinute) // Đặt phút là 0 (đúng đầu giờ)
        calendar.set(Calendar.SECOND, 0)
        // Nếu thời điểm này đã qua thì tăng thêm một ngày để lập lịch cho 9h sáng hôm sau
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        // Thiết lập báo thức với AlarmManager và PendingIntent tương ứng
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmMgr.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            } else {
                alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
    }
}

const val CHANNEL_ID = "notifyAlarm"
