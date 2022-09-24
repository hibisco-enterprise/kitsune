package com.hibisco.kitsune.feature.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import org.json.JSONObject

open class KitsuneViewModel: ViewModel() {
    private var job = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    private fun JSONObject.getIgnoreCase(key: String): String {
        keys().forEach {
            if (it.equals(key, true)) {
                return getString(it)
            }
        }
        return ""
    }

    companion object {
        const val TESTING_CODE = 299
    }

}