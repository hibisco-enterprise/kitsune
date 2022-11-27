package com.hibisco.kitsune.feature.ui.timeslots

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.hibisco.kitsune.databinding.ActivityTimeSlotsBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class TimeSlotsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimeSlotsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeSlotsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val timeSlots = listOf(TimeSlot("8:00"),
            TimeSlot("9:00"),
            TimeSlot("9:30"),
            TimeSlot("10:30"))

        val recyclerContainer = binding.recyclerTimeSlots
        recyclerContainer.layoutManager = LinearLayoutManager(
            baseContext,
            OrientationHelper.HORIZONTAL,
            false
        )

        recyclerContainer.adapter = TimeSlotsAdapter(timeSlots)

    }
}