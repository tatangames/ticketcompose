package com.tatanstudios.astropollococina.network

import com.santaananorte.ticket.model.ordenes.ModeloNuevasOrdenes
import com.santaananorte.ticket.model.ordenes.ModeloTicketPendiente
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {


    @GET("lista/ticket-pendientes")
    fun listadoTicketsPendientes(): Single<ModeloNuevasOrdenes>




}


