package com.kotlity.network.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BpiDto(
    @SerialName("USD")
    val usd: UsdDto
)