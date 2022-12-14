package com.hibisco.kitsune.feature.ui.history.util

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat
import java.util.*

class MyValueFormatter: ValueFormatter() {
    private val format = DecimalFormat("0")

    // override this for e.g. LineChart or ScatterChart
    override fun getPointLabel(entry: Entry?): String {
        return format.format(entry?.y)
    }

    override fun getFormattedValue(value: Float): String {
        return String.format(Locale("pt-BR"), "%.0f", value)
    }
}