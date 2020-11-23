package com.currencyconverter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.currencyconverter.model.CurrencyList
import com.currencyconverter.model.ExchangeLiveData
import com.currencyconverter.repository.CurrencyConverterRepository

class CurrencyConverterViewModel : ViewModel() {

    var currencyExchangeLiveData = MutableLiveData<ExchangeLiveData>()

    var currencyList = MutableLiveData<CurrencyList>()

    var error = MutableLiveData<String>()

    var isDataLoading = MutableLiveData<Boolean>()

    fun fetchCurrencyList() {
        isDataLoading.value = true
        CurrencyConverterRepository.fetchCurrencyList(object :
            CurrencyConverterRepository.OnDataFetch<CurrencyList> {
            override fun success(data: CurrencyList) {
                currencyList.value = data
                isDataLoading.value = false
            }

            override fun error(message: String) {
                error.value = message
                isDataLoading.value = false
            }

        })
    }

    fun fetchCurrencyExchangeRates() {
        isDataLoading.value = true
        CurrencyConverterRepository.fetchLiveData(object :
            CurrencyConverterRepository.OnDataFetch<ExchangeLiveData> {
            override fun success(data: ExchangeLiveData) {
                currencyExchangeLiveData.value = data
                isDataLoading.value = false
            }

            override fun error(message: String) {
                error.value = message
                isDataLoading.value = false
            }

        })
    }
}