package com.newsapp.service

import com.newsapp.entity.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApi {
    @GET("top-headlines")
    fun getNews(@Query("country") country: String = "us",
                @Query("apiKey") apiKey: String = ApiConstants.API_KEY,
                @Query("q") query: String,
                @Query("category") category: String): Call<NewsResponse>

}
