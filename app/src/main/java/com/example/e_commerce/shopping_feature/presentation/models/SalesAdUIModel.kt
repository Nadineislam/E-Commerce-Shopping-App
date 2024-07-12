package com.example.e_commerce.shopping_feature.presentation.models

import com.example.e_commerce.core.utils.CountdownTimer
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

data class SalesAdUIModel(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,

    var imageUrl: String? = null,

    val type: String? = null,

    var productId: String? = null,

    var categoryId: String? = null,

    var externalLink: String? = null,

    var endAt: Date? = null
) {

    private var timer: CountdownTimer? = null
    val hours = MutableStateFlow("")
    val seconds = MutableStateFlow("")
    val minutes = MutableStateFlow("")

    fun startCountdown() {
        endAt?.let {
            timer?.stop()
            timer = CountdownTimer(it) { hours, minutes, seconds ->
                this.hours.value = hours.toString()
                this.seconds.value = seconds.toString()
                this.minutes.value = minutes.toString()
            }
            timer?.start()
        }
    }

    fun stopCountdown() {
        timer?.stop()
    }
}

enum class SalesAdType {
    PRODUCT, CATEGORY, EXTERNAL_LINK, EMPTY
}