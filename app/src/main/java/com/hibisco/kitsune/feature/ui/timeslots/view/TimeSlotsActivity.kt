package com.hibisco.kitsune.feature.ui.timeslots.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.google.gson.Gson
import com.hibisco.kitsune.databinding.ActivityTimeSlotsBinding
import com.hibisco.kitsune.feature.ui.calendar.model.DateModel
import com.hibisco.kitsune.feature.ui.timeslots.delegate.TimeSlotsDelegate
import com.hibisco.kitsune.feature.ui.timeslots.viewModel.TimeSlotsViewModel

class TimeSlotsActivity : AppCompatActivity(), TimeSlotsDelegate {
    private lateinit var binding: ActivityTimeSlotsBinding
    private lateinit var date: DateModel
    private lateinit var viewModel: TimeSlotsViewModel
    private var idHospital: Long = 0
    private var chosenDate = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeSlotsBinding.inflate(layoutInflater)
        viewModel = TimeSlotsViewModel(this)
        setContentView(binding.root)
        setActions()
        setRecycleView()

        viewModel

        val dateString: String = intent.getStringExtra("date").toString()
        this.idHospital = intent.getLongExtra("idHospital", 0)

        date = gsonToDate(dateString)

        binding.tvDate.text = "${date.day}/${date.month}/${date.year}"
    }

    private fun setActions() {
        binding.imgArrow.setOnClickListener {
            finish()
        }

        binding.btnNext.setOnClickListener {
            viewModel.createAppointment(this.date, idHospital, 6, 9, 0)
        }
    }

    private fun setRecycleView() {
        val timeSlots = listOf(
            TimeSlot("8:00"),
            TimeSlot("9:00"),
            TimeSlot("9:30"),
            TimeSlot("10:30")
        )

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

    override fun onAppointmentCreated() {
        println("Sucessfull on creating appointment")
    }

    override fun onAppointmentCreationFailed() {
       println("Failed on creating appointment")
    }
}