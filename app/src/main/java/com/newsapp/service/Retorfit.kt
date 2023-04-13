package com.newsapp.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl("${ApiConstants.BASE_URL}/${ApiConstants.VERSION}/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
