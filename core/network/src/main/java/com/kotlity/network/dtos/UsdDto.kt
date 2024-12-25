package com.kotlity.network.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsdDto(
    @SerialName("rate_float")
    val rate: Float
)