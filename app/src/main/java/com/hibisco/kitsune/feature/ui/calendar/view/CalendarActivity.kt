package com.hibisco.kitsune.feature.ui.calendar.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hibisco.kitsune.R
import com.hibisco.kitsune.databinding.ActivityCalendarBinding

class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}