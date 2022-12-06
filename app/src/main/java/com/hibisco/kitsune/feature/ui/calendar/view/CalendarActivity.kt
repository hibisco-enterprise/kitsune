package com.hibisco.kitsune.feature.ui.calendar.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hibisco.kitsune.databinding.ActivityCalendarBinding
import com.hibisco.kitsune.feature.ui.calendar.model.DateModel
import com.hibisco.kitsune.feature.ui.timeslots.view.TimeSlotsActivity
import java.time.LocalDateTime

class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding
    private var idHospital: Long = 0
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var total: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idHospital = intent.getLongExtra("idHospital", 0)

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

        binding.btnNext.setOnClickListener {
            val timeSlots = Intent(applicationContext, TimeSlotsActivity::class.java)
            val date = DateModel(day, month, year)
            timeSlots.putExtra("date", this.dateToGson(date))
            timeSlots.putExtra("idHospital", this.idHospital)
            startActivity(timeSlots)
        }

    }

    private fun calculate() {
        year = LocalDateTime.now().year
        month = LocalDateTime.now().monthValue
        day = LocalDateTime.now().dayOfMonth
        total = (year * 360) + (month * 30) + day

        binding.tvDate.text = "$day/$month/$year"
    }


    fun dateToGson(date: DateModel?): String {
        val gson = Gson()
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonDate: String = gson.toJson(date)
        println(jsonDate)

        val jsonPretty: String = gsonPretty.toJson(date)
        println(jsonPretty)

        return jsonPretty
    }
}