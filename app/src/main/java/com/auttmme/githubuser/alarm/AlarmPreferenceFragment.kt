package com.auttmme.githubuser.alarm

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.auttmme.githubuser.R

class AlarmPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var alarmReminder: String
    private lateinit var alarmPreference: SwitchPreference
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        alarmReceiver = AlarmReceiver()
    }

    private fun init() {
        alarmReminder = resources.getString(R.string.key_alarm)
        alarmPreference = findPreference<SwitchPreference>(alarmReminder) as SwitchPreference
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == alarmReminder) {
            val alarm = sharedPreferences.getBoolean(key, true)

            if (alarm) {
                context?.let { alarmReceiver.setRepeatingAlarm(it, getString(R.string.reminder), getString(R.string.goBack)) }
            } else {
                context?.let { alarmReceiver.cancelAlarm(it, AlarmReceiver.TYPE_REPEATING) }
            }
        }
    }
}