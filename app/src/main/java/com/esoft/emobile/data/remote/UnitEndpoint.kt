package com.esoft.emobile.data.remote

import com.esoft.emobile.data.remote.model.GetUnitApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface UnitEndpoint {
    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("etiqueta-unidade/{unidade}")
    suspend fun verify(@Path("unidade") unit: String): Response<GetUnitApiResponse>
}
