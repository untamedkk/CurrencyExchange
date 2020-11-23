package com.currencyconverter

import java.math.BigDecimal
import java.math.RoundingMode

class CurrencyConverterCalculator {

//    FORMULA
//
//    x amount of sourceCurrency to destinationCurrency
//
//    1 sourceCurrencyInUsd = 1 / USD to sourceCurrency (we get from API)
//    1 destinationCurrencyInUsd = 1 / USD to destinactionCurrenct (we get from API)
//
//    (1 sourceCurrency to destinationCurreny)margin = sourceCurrencyInUsd/destinationCurrencyInUsd
//
//    (1 destinationCurreny to sourceCurrency)margin = destinationCurrencyInUsd/sourceCurrencyInUsd
//
//    destinationCurrency = x amount of sourceCurrencyInUsd * margin
//
//    ================ EXAMPLE ============================
//
//    75.90 PKR to NPR = ?
//
//    1 PKR = 0.006220825301341 USD
//    1 NPR = 0.008432974194045 USD
//
//    (1 PKR to NPR) margin = 0.737678683486768 * 75.90 = 55.989812076645691 NPR

    companion object {

        const val BASE_CURRENCY = "USD"

        fun convert(
            source: Double?,
            destination: Double,
            amount: Double
        ): Double {

            val oneSourceCurrencyInUsd = 1 / source!!
            val oneDestinationCurrencyInUsd = 1 / destination

            val sourceMargin = oneSourceCurrencyInUsd / oneDestinationCurrencyInUsd
            val destinationMargin = oneDestinationCurrencyInUsd / oneSourceCurrencyInUsd // for destination to source

            return BigDecimal(amount * sourceMargin).setScale(2, RoundingMode.HALF_EVEN).toDouble()
        }
    }

}