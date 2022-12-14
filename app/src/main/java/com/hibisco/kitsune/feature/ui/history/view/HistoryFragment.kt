package com.hibisco.kitsune.feature.ui.history.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.EntryXComparator
import com.google.gson.Gson
import com.hibisco.kitsune.R
import com.hibisco.kitsune.databinding.ActivityHistoryBinding
import com.hibisco.kitsune.feature.network.model.*
import com.hibisco.kitsune.feature.ui.history.adapter.HistoryAdapter
import com.hibisco.kitsune.feature.ui.history.delegate.HistoryDelegate
import com.hibisco.kitsune.feature.ui.history.model.BeautifulAppointment
import com.hibisco.kitsune.feature.ui.history.model.DonationData
import com.hibisco.kitsune.feature.ui.history.util.MyValueFormatter
import com.hibisco.kitsune.feature.ui.history.viewModel.HistoryViewModel
import java.time.format.DateTimeFormatter
import java.util.*


class HistoryFragment : Fragment(R.layout.activity_history), HistoryDelegate {

    lateinit var viewModel: HistoryViewModel

    private var _binding: ActivityHistoryBinding? = null
    private val binding get() = _binding!!

    lateinit var loggedUser: Donator

    var beautifiedHistory: List<BeautifulAppointment> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityHistoryBinding.inflate(inflater, container, false)

        loggedUser = getUserPreferences()

        viewModel = HistoryViewModel(this)

        viewModel.getAppointments(loggedUser.idDonator)

        val chart = binding.chart

        var entries: MutableList<Entry> = ArrayList()

        for (element in beautifiedHistory){
            entries.add(Entry(element.year.toFloat(), element.donationData.size.toFloat()))
        }
        if (entries.size < 6){
            var lastYear = if (beautifiedHistory.isNotEmpty()) beautifiedHistory.last().year.toFloat() else Calendar.getInstance().get(Calendar.YEAR).toFloat()
            val diference = 6 - entries.size
            for (i in 0 until diference){
                entries.add(Entry(if (beautifiedHistory.isNotEmpty()) --lastYear else lastYear--, 0f))
            }
        }
        Collections.sort(entries, EntryXComparator())

        var dataset = LineDataSet(entries, "Quantidade")
        dataset.color = ContextCompat.getColor(activity!!, R.color.secondary_red)
        dataset.setCircleColor(ContextCompat.getColor(activity!!, R.color.secondary_red))
        dataset.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataset.setDrawFilled(true)
        dataset.fillColor = ContextCompat.getColor(activity!!, R.color.secondary_red)
        dataset.fillAlpha = 50
        dataset.valueTypeface = ResourcesCompat.getFont(activity!!, R.font.outfit_medium)

        var lineData = LineData(dataset)
        lineData.setValueFormatter(MyValueFormatter())
        lineData.setDrawValues(false)
        chart.data = lineData
        chart.axisRight.isEnabled = false
        chart.axisLeft.granularity = 1.0f
        chart.axisLeft.isGranularityEnabled = true
        chart.axisLeft.typeface = ResourcesCompat.getFont(activity!!, R.font.outfit_medium)
        chart.xAxis.granularity = 1f
        chart.xAxis.isGranularityEnabled = true
        chart.xAxis.typeface = ResourcesCompat.getFont(activity!!, R.font.outfit_medium)
        chart.xAxis.valueFormatter = MyValueFormatter()
        chart.legend.typeface = ResourcesCompat.getFont(activity!!, R.font.outfit_medium)
        chart.description.isEnabled = false
        chart.setVisibleYRange(0f, 5f, chart.axisLeft.axisDependency)
        chart.invalidate()

        // Inflate the layout for this fragment
        return binding.root
    }

    fun beautifyHistory(appointments: List<Appointment>){
        var beautifiedList: MutableList<BeautifulAppointment> = ArrayList()

        for((index, appointment) in appointments.withIndex()){
            if (index == 0) {
                beautifiedList.add(
                    BeautifulAppointment(
                        appointment.dhAppointment.year.toString(),
                        mutableListOf(
                            DonationData(
                                DateTimeFormatter.ofPattern("dd/MM").format(appointment.dhAppointment),
                                DateTimeFormatter.ofPattern("hh:mm").format(appointment.dhAppointment),
                                appointment.hospital.user.name
                            )
                        )
                    )
                )
            } else {
                if (appointment.dhAppointment.year != appointments[index -1].dhAppointment.year){
                    beautifiedList.add(
                        BeautifulAppointment(
                            appointment.dhAppointment.year.toString(),
                            mutableListOf(
                                DonationData(
                                    DateTimeFormatter.ofPattern("dd/MM").format(appointment.dhAppointment),
                                    DateTimeFormatter.ofPattern("hh:mm").format(appointment.dhAppointment),
                                    appointment.hospital.user.name
                                )
                            )
                        )
                    )
                } else {
                    beautifiedList[beautifiedList.size - 1].donationData.add(
                        DonationData(
                            DateTimeFormatter.ofPattern("dd/MM").format(appointment.dhAppointment),
                            DateTimeFormatter.ofPattern("hh:mm").format(appointment.dhAppointment),
                            appointment.hospital.user.name
                        )
                    )
                }
            }
        }

        beautifiedHistory = beautifiedList
    }

    fun getUserPreferences(): Donator {
        val sharedPreference =  activity!!.getSharedPreferences("USER_DATA", 0)
        val gsonString: String? = sharedPreference.getString("userModelString","defaultUser")
        val gson = Gson()
        val model: Donator = gson.fromJson(gsonString, Donator::class.java)
        println("> From JSON String:\n" + model)

        return model
    }

    fun configRecyclerView(appointments: List<Appointment>){

        val recyclerContainer = binding.historyRc
        recyclerContainer.layoutManager = LinearLayoutManager(
            activity!!
        )
        recyclerContainer.adapter = HistoryAdapter(
            appointments
        )
    }

    override fun appointmentsResponse(appointments: List<Appointment>) {
        configRecyclerView(appointments)
        beautifyHistory(appointments)
    }

    override fun getAppointmentsFailed(error: String) {
        Toast.makeText(activity!!, error, Toast.LENGTH_SHORT)
    }
}


