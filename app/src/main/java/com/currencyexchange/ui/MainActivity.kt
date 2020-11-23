package com.currencyexchange.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.currencyconverter.model.CurrencyList
import com.currencyconverter.model.ExchangeLiveData
import com.currencyconverter.networking.RetrofitClient
import com.currencyconverter.viewmodel.CurrencyConverterViewModel
import com.currencyexchange.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: CurrencyConverterViewModel

    private lateinit var currencySpinner: AppCompatSpinner
    private lateinit var exchangeRateGrid: RecyclerView
    private lateinit var amountEditText: AppCompatEditText

    private lateinit var currencyList: CurrencyList
    private lateinit var exchangeLiveData: ExchangeLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(CurrencyConverterViewModel::class.java)

        RetrofitClient.setCacheDirectory(applicationContext.cacheDir)

        fetchData()
        initViews()
        postInitViews()
        setObservers()
        setErrorObservers()
    }

    private fun fetchData() {
        viewModel.fetchCurrencyList()
        viewModel.fetchCurrencyExchangeRates()
    }

    private fun initViews() {
        currencySpinner = findViewById(R.id.currency_selector_spinner)
        exchangeRateGrid = findViewById(R.id.exchange_rate_recycle_view)
        amountEditText = findViewById(R.id.edit_text_currency)
    }

    private fun setObservers() {
        viewModel.currencyList.observe(this, Observer {
            currencyList = it
            fillCurrencyListData(it)
        })
        viewModel.currencyExchangeLiveData.observe(this, Observer {
            setCurrentLiveData(it)
            exchangeLiveData = it
        })
    }

    private fun setErrorObservers() =
        viewModel.error.observe(this, Observer { Log.e("Error", it) })

    private fun fillCurrencyListData(currencyList: CurrencyList) {
        val values = currencyList.currencies.values.toList()
        val currencyListAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, values)
        currencySpinner.adapter = currencyListAdapter
    }

    private fun setCurrentLiveData(currentLiveData: ExchangeLiveData) {
        val adapter = exchangeRateGrid.adapter as ExchangeRateGridAdapter
        adapter.setExchangeData(currentLiveData)
    }

    private fun postInitViews() {
        exchangeRateGrid.adapter = ExchangeRateGridAdapter()

        exchangeRateGrid.addItemDecoration(
            DividerItemDecoration(
                this@MainActivity,
                DividerItemDecoration.HORIZONTAL
            )
        )
        exchangeRateGrid.addItemDecoration(
            DividerItemDecoration(
                this@MainActivity,
                DividerItemDecoration.VERTICAL
            )
        )
        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val currencies = currencyList.currencies

                (exchangeRateGrid.adapter as ExchangeRateGridAdapter).refresh(
                    currencies.keys.toList()[position],
                    if (TextUtils.isEmpty(amountEditText.text)) 1.0 else amountEditText.text.toString()
                        .toDouble()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }
}