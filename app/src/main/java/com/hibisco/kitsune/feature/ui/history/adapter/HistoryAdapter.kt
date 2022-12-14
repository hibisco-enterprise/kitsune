package com.hibisco.kitsune.feature.ui.history.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hibisco.kitsune.R
import com.hibisco.kitsune.feature.network.model.Appointment
import java.time.format.DateTimeFormatter

class HistoryAdapter(val appointments: List<Appointment>) : RecyclerView.Adapter<ViewHolder>(){

    private lateinit var listener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick (position: Int)
    }

    fun setOnClickListener (listener: onItemClickListener) {
        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_history, parent, false)
        return HistoryHolder(view, this.listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as HistoryHolder).bind(appointments[position])
    }

    override fun getItemCount(): Int {
        return appointments.size
    }


    inner class HistoryHolder(private val historyLayout: View, listener: onItemClickListener ) : ViewHolder(historyLayout) {

        fun bind(appointment: Appointment) {

            val tvLocal = historyLayout
                .findViewById<TextView>(R.id.tvHistoryLocal)

            val tvDate = historyLayout
                .findViewById<TextView>(R.id.tvHistoryDate)

            tvLocal.text = appointment.hospital.user.name
            tvDate.text = appointment.dhAppointment.format(DateTimeFormatter.ofPattern("dd/MMMM/yyyy - hh:mm")).replace("/", " de ")
        }

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}