package com.santaananorte.ticket.model.ordenes

import com.google.gson.annotations.SerializedName


data class ModeloNuevasOrdenes(
    @SerializedName("success") val success: Int,
    @SerializedName("data") val lista: List<ModeloTicketPendiente>
)

data class ModeloTicketPendiente(
    @SerializedName("id") val id: Int,
    @SerializedName("id_usuario") val idUsuario: Int,
    @SerializedName("fecha_registro") val fechaRegistro: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("tipo_incidente") val tipoIncidente: String?,
    @SerializedName("sistema_afectado") val sistemaAfectado: String?,
    @SerializedName("nivel") val nivel: Int,
    @SerializedName("medida_correctivas") val medidaCorrectivas: String?,
    @SerializedName("observaciones") val observaciones: String?,
    @SerializedName("estado") val estado: Int,
    @SerializedName("fecha_solucionado") val fechaSolucionado: String?,
    @SerializedName("nombre_unidad") val nombreUnidad: String,
)


