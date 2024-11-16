package com.esoft.emobile.data.remote.model

import com.esoft.emobile.domain.model.Exists
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetVehicleApiResponse(
    @Json(name = "exists")
    val exists: Boolean,
)


fun GetVehicleApiResponse.asDomainModel(): Exists {
    return Exists(
        exists = exists
    )

}