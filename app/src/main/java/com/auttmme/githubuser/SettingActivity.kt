package com.auttmme.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.auttmme.githubuser.alarm.AlarmPreferenceFragment
import com.auttmme.githubuser.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, AlarmPreferenceFragment()).commit()
    }
}