package com.esoft.emobile.data.remote.service.concerns

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.util.regex.Pattern

interface ServiceRequest {

    suspend fun <T> safeNoBodyApiCall(apiCall: suspend () -> retrofit2.Response<T>): Result<T?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    Result.success(null)
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

    suspend fun <T> safeApiCall(apiCall: suspend () -> retrofit2.Response<T>): Result<T?> {

        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (response.code() == 201) {
                            return@let Result.success(null)
                        }
                        Result.success(it)
                    } ?: Result.failure(Exception("Empty response body"))
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

    fun hydrateResponse(it: ResponseBody): String {
        val body = it.string()
        val safe = body.decodeUnicodeEscapes()

        // Ajuste da expressão regular para capturar textos entre [" "]
        val pattern = Pattern.compile("\\[\"(.*?)\"\\]")
        val matcher = pattern.matcher(safe)

        val result = StringBuilder()
        while (matcher.find()) {
            if (result.isNotEmpty()) {
                result.append(";")
            }
            result.append(matcher.group(1)) // Adiciona o texto dentro dos colchetes e aspas duplas
        }

        println(result.toString()) // Opcional: imprime o resultado concatenado
        return result.toString()
    }

    fun String.decodeUnicodeEscapes(): String {
        return try {
            // Substitui sequências Unicode (\uXXXX) por seus respectivos caracteres
            this.replace(Regex("\\\\u([0-9A-Fa-f]{4})")) {
                val codePoint = it.groupValues[1].toInt(16)
                String(Character.toChars(codePoint))
            }
        } catch (e: Exception) {
            println("Erro ao decodificar a string: ${e.message}")
            this // Retorna a string original em caso de erro
        }
    }
}