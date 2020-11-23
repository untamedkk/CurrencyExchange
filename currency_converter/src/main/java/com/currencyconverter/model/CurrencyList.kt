package com.currencyconverter.model

class CurrencyList(
    success: Boolean,
    terms: String,
    privacy: String,
    val currencies: LinkedHashMap<String, String>, error: Error
) : BaseData(success, terms, privacy, error)