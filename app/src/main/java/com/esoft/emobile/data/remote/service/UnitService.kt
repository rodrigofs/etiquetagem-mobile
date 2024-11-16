package com.rodrigofs.etiquetaskotlin.network

import com.esoft.emobile.data.remote.UnitEndpoint
import com.esoft.emobile.data.remote.model.GetUnitApiResponse
import com.esoft.emobile.data.remote.service.concerns.ServiceRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UnitService @Inject constructor(
    private val unitApi: UnitEndpoint
) : ServiceRequest {

    suspend fun verify(unit: String): Result<GetUnitApiResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = unitApi.verify(unit)
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