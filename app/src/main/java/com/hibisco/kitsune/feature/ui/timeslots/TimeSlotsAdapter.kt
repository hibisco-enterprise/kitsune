package com.hibisco.kitsune.feature.ui.timeslots

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hibisco.kitsune.R

class TimeSlotsAdapter(val slots: List<TimeSlot>) : RecyclerView.Adapter<ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.res_card_time_slot, parent, false)
        return TimeSlotHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as TimeSlotHolder).bind(slots[position])
    }

    override fun getItemCount(): Int {
        return slots.size
    }

    inner class TimeSlotHolder(private val timeSlotLayout: View) : ViewHolder(timeSlotLayout) {

        fun bind(timeSlot: TimeSlot) {

            val tvTimeSlot = timeSlotLayout
                .findViewById<TextView>(R.id.time_slot)

            tvTimeSlot.text = timeSlot.time.toString()
        }
    }
}