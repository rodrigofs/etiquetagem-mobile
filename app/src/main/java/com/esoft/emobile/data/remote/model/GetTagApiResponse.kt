package com.esoft.emobile.data.remote.model

import com.esoft.emobile.domain.model.Nf
import com.esoft.emobile.domain.model.Tag
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class GetTagApiResponse(
    @Json(name = "etiqueta")
    val etiqueta: String = "",

    @Json(name = "chave_acesso")
    val chaveAcesso: String = "",

    @Json(name = "nf")
    val nf: String = "",

    @Json(name = "serie")
    val serie: String = "",

    @Json(name = "volumes")
    val volumes: Int = 0,

    @Json(name = "item")
    val item: String = "",

    @Json(name = "emit_cnpj")
    val emitCnpj: String = "",

    @Json(name = "emit_nome")
    val emitNome: String = "",

    @Json(name = "emit_cidade")
    val emitCidade: String = "",

    @Json(name = "emit_uf")
    val emitUf: String = "",

    @Json(name = "dest_cnpj")
    val destCnpj: String = "",

    @Json(name = "dest_nome")
    val destNome: String = "",

    @Json(name = "dest_logradouro")
    val destLogradouro: String = "",

    @Json(name = "dest_numero")
    val destNumero: String = "",

    @Json(name = "dest_cep")
    val destCep: String = "",

    @Json(name = "dest_cidade")
    val destCidade: String = "",

    @Json(name = "dest_uf")
    val destUf: String = "",

    @Json(name = "placa")
    val placa: String = "",

    @Json(name = "org_sigla")
    val orgSigla: String = "",

    @Json(name = "dst_sigla")
    val dstSigla: String = "",

    @Json(name = "dst_rota")
    val dstRota: String = "",

    @Json(name = "org_titulo")
    val orgTitulo: String = "",

    )


fun GetTagApiResponse.asDomainModel(tags: List<GetTagApiResponse>?): Nf {

    if (tags == null) {
        return Nf()
    }

    val index = 0

    return Nf(
        chaveAcesso = tags[index].chaveAcesso,
        nf = tags[index].nf,
        serie = tags[index].serie,
        volumes = tags[index].volumes,
        emitCnpj = tags[index].emitCnpj,
        emitNome = tags[index].emitNome,
        emitCidade = tags[index].emitCidade,
        emitUf = tags[index].emitUf,
        destCnpj = tags[index].destCnpj,
        destNome = tags[index].destNome,
        destLogradouro = tags[index].destLogradouro,
        destNumero = tags[index].destNumero,
        destCep = tags[index].destCep,
        destCidade = tags[index].destCidade,
        destUf = tags[index].destUf,
        orgSigla = tags[index].orgSigla,
        dstSigla = tags[index].dstSigla,

        etiquetas = tags.map {
            Tag(
                etiqueta = it.etiqueta,
                chaveAcesso = it.chaveAcesso,
                nf = it.nf,
                serie = it.serie,
                volumes = it.volumes,
                item = it.item,
                emitCnpj = it.emitCnpj,
                emitNome = it.emitNome,
                emitCidade = it.emitCidade,
                emitUf = it.emitUf,
                destCnpj = it.destCnpj,
                destNome = it.destNome,
                destLogradouro = it.destLogradouro,
                destNumero = it.destNumero,
                destCep = it.destCep,
                destCidade = it.destCidade,
                destUf = it.destUf,
                orgSigla = it.orgSigla,
                dstSigla = it.dstSigla,
                dstRota = it.dstRota,
                orgTitulo = it.orgTitulo
            )
        }
    )

}