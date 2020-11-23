package com.currencyconverter.model

class ExchangeLiveData(
    val timestamp: Long,
    val source: String,
    val quotes: Map<String, Double>,
    success: Boolean,
    terms: String,
    privacy: String, error: Error
) : BaseData(success, terms, privacy, error)