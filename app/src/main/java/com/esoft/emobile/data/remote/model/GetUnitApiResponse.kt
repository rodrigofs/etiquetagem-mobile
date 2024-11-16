package com.esoft.emobile.data.remote.model

import com.esoft.emobile.domain.model.Exists
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetUnitApiResponse(
    @Json(name = "exists")
    val exists: Boolean,
)


fun GetUnitApiResponse.asDomainModel(): Exists {
    return Exists(
        exists = exists
    )

}