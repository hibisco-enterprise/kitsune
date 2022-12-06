package com.hibisco.kitsune.feature.ui.timeslots

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.google.gson.Gson
import com.hibisco.kitsune.databinding.ActivityTimeSlotsBinding
import com.hibisco.kitsune.feature.network.model.Hospital
import com.hibisco.kitsune.feature.ui.calendar.model.DateModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class TimeSlotsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTimeSlotsBinding
    private lateinit var date: DateModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeSlotsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActions()
        setRecycleView()

        val dateString: String = intent.getStringExtra("date").toString()
        date = gsonToDate(dateString)

        binding.tvDate.text = "${date.day}/${date.month}/${date.year}"
    }

    private fun setActions() {
        binding.imgArrow.setOnClickListener {
            finish()
        }
    }

    private fun setRecycleView() {
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

    fun gsonToDate(dateJson: String): DateModel {
        val gson = Gson()

        val date: DateModel = gson.fromJson(dateJson, DateModel::class.java)
        println("> From JSON String:\n" + date)

        return date
    }
}