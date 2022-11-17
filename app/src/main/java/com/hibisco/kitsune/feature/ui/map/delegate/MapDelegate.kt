package com.hibisco.kitsune.feature.ui.map.delegate

import com.hibisco.kitsune.feature.network.model.BloodTypeStock
import com.hibisco.kitsune.feature.network.model.Hospital

interface MapDelegate {
    fun hospitalsResponse(hospitals: List<Hospital>)
    fun getHospitalsFailed(error: String)

    fun bloodStockResponse(bloodStock: List<BloodTypeStock>)
    fun getBloodStockFailed(error: String)
}