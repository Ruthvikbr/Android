package com.example.donationui.model

import androidx.annotation.DrawableRes

data class Donation(
    val title: String,
    @DrawableRes val iconId: Int
)
