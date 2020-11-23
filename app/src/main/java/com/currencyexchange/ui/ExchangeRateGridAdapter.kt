package com.currencyexchange.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.currencyconverter.CurrencyConverterCalculator
import com.currencyconverter.model.ExchangeLiveData
import com.currencyexchange.R

class ExchangeRateGridAdapter :
    RecyclerView.Adapter<ExchangeRateGridAdapter.GridViewHolder>() {

    private lateinit var currentLiveData: ExchangeLiveData
    private lateinit var currency: String
    private var amount: Double = 1.0

    private var rateList = listOf<Double>()
    private lateinit var resultList: Array<Double?>
    private var keys = listOf<String>()

    class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.rate_text_view)
    }

    fun setExchangeData(currentLiveData: ExchangeLiveData) {
        this.currentLiveData = currentLiveData
        rateList = currentLiveData.quotes.values.toList()
        keys = currentLiveData.quotes.keys.toList()
        resultList = arrayOfNulls(rateList.size)
    }

    fun refresh(currency: String, amount: Double) {
        this.currency = currency
        this.amount = amount
        resultList = arrayOfNulls(rateList.size)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_item_view, parent, false)
        return GridViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        var result = resultList[position]
        if (result == null) {
            result = CurrencyConverterCalculator.convert(
                currentLiveData.quotes[CurrencyConverterCalculator.BASE_CURRENCY + currency],
                rateList[position],
                amount
            )
            resultList[position] = result //Caching the result
        }

        holder.textView.text = "$amount $currency = $result ${keys[position].substring(3)}"
    }

    override fun getItemCount(): Int {
        return rateList.size
    }
}