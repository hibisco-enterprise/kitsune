package com.hibisco.kitsune.feature.ui.map.viewModel

import com.hibisco.kitsune.feature.network.RetroFitInstance
import com.hibisco.kitsune.feature.ui.base.KitsuneViewModel
import com.hibisco.kitsune.feature.ui.map.delegate.MapDelegate

class MapViewModel(val delegate: MapDelegate): KitsuneViewModel() {
    private val retrofit = RetroFitInstance.getRetrofitKitsune()
}