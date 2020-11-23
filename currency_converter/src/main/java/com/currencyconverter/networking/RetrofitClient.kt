package com.currencyconverter.networking

import android.content.Context
import com.currency_converter.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


object RetrofitClient {

    private const val cacheSize = (10 * 1024 * 1024).toLong() // 10Mb cache size
    private const val cacheAge = 60 * 30 //Cache TTL in minutes

    private var cache: Cache? = null
    private var context: Context? = null

    private val retrofitClient: Retrofit.Builder by lazy {

        val okHttpClient = OkHttpClient.Builder()

        okHttpClient.addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }).addInterceptor { it ->
            val request = it.request()
            request.newBuilder().header("Cache-Control", "public, max-age=$cacheAge").build()
            it.proceed(request)
        }.cache(cache)

        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
    }

    fun setCacheDirectory(cacheDir: File) {
        this.cache = Cache(cacheDir, cacheSize)
    }

    val services: Services by lazy {
        retrofitClient
            .build()
            .create(Services::class.java)
    }
}