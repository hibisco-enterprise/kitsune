package com.hibisco.kitsune.feature.ui.map.delegate

import com.hibisco.kitsune.feature.network.model.Hospital

interface MapDelegate {
    fun hospitalsResponse(hospitals: List<Hospital>)
    fun getHospitalsFailed(error: String)
}