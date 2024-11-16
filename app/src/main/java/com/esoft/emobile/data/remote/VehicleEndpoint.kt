package com.esoft.emobile.data.remote;

import com.esoft.emobile.data.remote.model.GetVehicleApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface VehicleEndpoint {
    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("etiqueta-veiculo/{placa}")
    suspend fun verify(@Path("placa") placa: String): Response<GetVehicleApiResponse>;
}
