package com.hibisco.kitsune.feature.ui.timeslots.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import com.hibisco.kitsune.R
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
    private var hour: Int = 0
    private var minute: Int = 0
    private lateinit var donator: Donator

    val timeSlots = listOf(
        TimeSlot("8:00", 8, 0),
        TimeSlot("8:30", 8, 30),
        TimeSlot("9:00", 9, 0),
        TimeSlot("9:30", 9, 30),
        TimeSlot("10:00", 10, 0),
        TimeSlot("10:30", 10, 30),
        TimeSlot("11:00", 11, 0),
        TimeSlot("11:30", 11, 30),
        TimeSlot("12:00", 12, 0),
        TimeSlot("12:30", 12, 30),
        TimeSlot("13:00", 13, 0),
        TimeSlot("13:30", 13, 30),
        TimeSlot("14:00", 14, 0),
        TimeSlot("14:30", 14, 30),
        TimeSlot("15:00", 15, 0),
        TimeSlot("15:30", 15, 30),
        TimeSlot("16:00", 16, 0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeSlotsBinding.inflate(layoutInflater)
        viewModel = TimeSlotsViewModel(this)
        setContentView(binding.root)
        setActions()
        setRecycleView()

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
            donator = getUserPreferences()
            viewModel.createAppointment(this.date, idHospital, donator.idDonator, hour, minute)

        }
    }

    private fun setRecycleView() {
        val recyclerContainer = binding.recyclerTimeSlots
        recyclerContainer.layoutManager = GridLayoutManager(
            baseContext,
            3
        )

        val adapter = TimeSlotsAdapter(timeSlots)
        recyclerContainer.adapter = adapter

        adapter.setOnClickListener(object : TimeSlotsAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                hour = timeSlots[position].hour
                minute = timeSlots[position].minute

                binding.tvTime.text = timeSlots[position].time

                Toast.makeText(this@TimeSlotsActivity, "${timeSlots[position].time}", Toast.LENGTH_SHORT).show()
            }
        })
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