package com.hibisco.kitsune.feature.ui.timeslots.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hibisco.kitsune.R
import java.util.Collections

class TimeSlotsAdapter(val slots: List<TimeSlot>) : RecyclerView.Adapter<ViewHolder>(){

    private lateinit var listener: onItemClickListener
    private var holders: ArrayList<TimeSlotHolder>? = null

    interface onItemClickListener {
        fun onItemClick (position: Int)
    }

    fun setOnClickListener (listener: onItemClickListener) {
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.res_card_time_slot, parent, false)
        return TimeSlotHolder(view, this.listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as TimeSlotHolder).bind(slots[position])
        holders?.add(holder)
    }

    override fun getItemCount(): Int {
        return slots.size
    }


    inner class TimeSlotHolder(private val timeSlotLayout: View, listener: onItemClickListener ) : ViewHolder(timeSlotLayout) {

        fun bind(timeSlot: TimeSlot) {

            val tvTimeSlot = timeSlotLayout
                .findViewById<TextView>(R.id.time_slot)

            tvTimeSlot.text = timeSlot.time
        }

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}