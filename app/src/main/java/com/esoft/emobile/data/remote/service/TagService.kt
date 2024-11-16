package com.rodrigofs.etiquetaskotlin.network

import com.esoft.emobile.data.remote.TagEndpoint
import com.esoft.emobile.data.remote.model.GetTagApiResponse
import com.esoft.emobile.data.remote.service.concerns.ServiceRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TagService @Inject constructor(
    private val tagApi: TagEndpoint
) : ServiceRequest {

    suspend fun requestTags(
        chaveAcesso: String,
        unidade: String,
        placa: String,
        volumes: String
    ): Result<List<GetTagApiResponse>?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = tagApi.findByAccessKey(chaveAcesso, unidade, placa, volumes)

                if (response.isSuccessful && response.code() == 200) {
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