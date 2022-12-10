package com.hibisco.kitsune.feature.ui.timeslots.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.google.gson.Gson
import com.hibisco.kitsune.databinding.ActivityTimeSlotsBinding
import com.hibisco.kitsune.feature.network.model.Donator
import com.hibisco.kitsune.feature.ui.base.MainActivity
import com.hibisco.kitsune.feature.ui.calendar.model.DateModel
import com.hibisco.kitsune.feature.ui.timeslots.delegate.TimeSlotsDelegate
import com.hibisco.kitsune.feature.ui.timeslots.viewModel.TimeSlotsViewModel


class TimeSlotsActivity : AppCompatActivity(), TimeSlotsDelegate {
    private lateinit var binding: ActivityTimeSlotsBinding
    private lateinit var date: DateModel
    private lateinit var viewModel: TimeSlotsViewModel
    private var idHospital: Long = 0
    private var chosenDate = 0
    private lateinit var donator: Donator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeSlotsBinding.inflate(layoutInflater)
        viewModel = TimeSlotsViewModel(this)
        setContentView(binding.root)
        setActions()
        setRecycleView()
        donator = getUserPreferences()

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
            viewModel.createAppointment(this.date, idHospital, donator.idDonator, 9, 0)
            this.onAppointmentCreated()
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

    fun getUserPreferences(): Donator {
        val sharedPreference =  getSharedPreferences("USER_DATA", 0)
        val gsonString: String? = sharedPreference.getString("userModelString","defaultUser")
        val gson = Gson()
        val model: Donator = gson.fromJson(gsonString, Donator::class.java)
        println("> From JSON String:\n" + model)

        return model
    }

    override fun onAppointmentCreated() {
        println("Sucessfull on creating appointment")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val main = Intent(this, MainActivity::class.java)
        main.putExtra("shouldOpenModal", true)
        startActivity(main)
    }

    override fun onAppointmentCreationFailed() {
       println("Failed on creating appointment")
    }
}