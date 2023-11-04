package com.brigida.projetodeteste.data

import com.brigida.projetodeteste.data.model.CurrentWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    //weather?q={cidade}&appid={chave da api}&units={unidades de medida}&lang={linguagem escolhida}&timezone={fuso horario}
    @GET("weather?")
    fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String,
        @Query("lang") language: String
    ): Call<CurrentWeather>
}