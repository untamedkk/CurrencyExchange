package com.currencyconverter.networking

import com.currencyconverter.model.CurrencyList
import com.currencyconverter.model.ExchangeLiveData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Services {

    @GET("/live")
    fun fetchLiveData(@Query("access_key") access_key: String): Call<ExchangeLiveData>

    @GET("/list")
    fun fetchCurrencyList(@Query("access_key") access_key: String): Call<CurrencyList>
}