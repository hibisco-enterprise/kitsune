package com.hibisco.kitsune.feature.ui.calendar.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hibisco.kitsune.R
import com.hibisco.kitsune.databinding.ActivityCalendarBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var total: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActions()
        calculate()
    }

    private fun setActions() {
        binding.imgArrow.setOnClickListener {
            finish() }

        binding.calendarView.setOnDateChangeListener {
            calendarView, year, month, day ->
            this.year = year
            this.month = month + 1
            this.day = day

            binding.tvDate.text = "$day/${month+1}/$year"
        }

    }

    private fun calculate() {
        year = LocalDateTime.now().year
        month = LocalDateTime.now().monthValue
        day = LocalDateTime.now().dayOfMonth
        total = (year * 360) + (month * 30) + day

        binding.tvDate.text = "$day/$month/$year"
    }
}