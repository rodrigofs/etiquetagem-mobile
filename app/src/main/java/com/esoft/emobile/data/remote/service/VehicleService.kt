package com.rodrigofs.etiquetaskotlin.network

import com.esoft.emobile.data.remote.VehicleEndpoint
import com.esoft.emobile.data.remote.model.GetVehicleApiResponse
import com.esoft.emobile.data.remote.service.concerns.ServiceRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VehicleService @Inject constructor(
    private val vehicleApi: VehicleEndpoint
) : ServiceRequest {

    suspend fun verify(plate: String): Result<GetVehicleApiResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = vehicleApi.verify(plate)
                if (response.isSuccessful && response.code() == 200 || response.code() == 404) {
                    response.body()?.let {
                        return@let Result.success(response.body())
                    } ?: Result.failure(Exception("Erro desconhecido."))
                } else {
                    response.errorBody()?.let {

                        val body = hydrateResponse(it)

                        Result.failure(Exception(body))
                    } ?: Result.failure(Exception("Erro desconhecido."))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}