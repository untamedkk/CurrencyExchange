package com.currencyconverter.repository

import android.util.Log
import com.currency_converter.BuildConfig
import com.currencyconverter.model.CurrencyList
import com.currencyconverter.model.ExchangeLiveData
import com.currencyconverter.networking.RetrofitClient
import com.currencyconverter.networking.Services
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object CurrencyConverterRepository {

    private val TAG = CurrencyConverterRepository::class.java.simpleName

    private val services: Services by lazy { RetrofitClient.services }

    fun fetchLiveData(onDataFetchExchange: OnDataFetch<ExchangeLiveData>) {

        val call = services.fetchLiveData(BuildConfig.API_KEY)

        call.enqueue(object : Callback<ExchangeLiveData> {
            override fun onResponse(
                call: Call<ExchangeLiveData>,
                response: Response<ExchangeLiveData>
            ) {
                response.body()?.let { onDataFetchExchange.success(it) }
            }

            override fun onFailure(call: Call<ExchangeLiveData>, t: Throwable) {
                onDataFetchExchange.error(t.localizedMessage)
            }
        })
    }

    fun fetchCurrencyList(onDataFetch: OnDataFetch<CurrencyList>) {
        val call = services.fetchCurrencyList(BuildConfig.API_KEY)

        call.enqueue(object : Callback<CurrencyList> {
            override fun onResponse(call: Call<CurrencyList>, response: Response<CurrencyList>) {
                response.body()?.let {
                    if (it.success) {
                        onDataFetch.success(it)
                    } else {
                        onDataFetch.error(it.error.info)
                    }
                }
            }

            override fun onFailure(call: Call<CurrencyList>, t: Throwable) {
                Log.e(TAG, t.localizedMessage)
                onDataFetch.error(t.localizedMessage)
            }
        })
    }

    interface OnDataFetch<T> {
        fun success(data: T)
        fun error(message: String)
    }

}