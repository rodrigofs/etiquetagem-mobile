package com.esoft.emobile.data.remote;

import com.esoft.emobile.data.remote.model.GetTagApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface TagEndpoint {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("etiqueta-impressa/{chave_acesso}/{unidade}/{placa}/{volumes}")
    suspend fun findByAccessKey(
        @Path("chave_acesso") chave_acesso: String,
        @Path("unidade") unidade: String,
        @Path("placa") placa: String,
        @Path("volumes") volumes: String
    ): Response<List<GetTagApiResponse>>
}
