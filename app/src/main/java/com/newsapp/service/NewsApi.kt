package com.newsapp.service

import com.newsapp.entity.NewsResponse
import retrofit2.Call
import retrofit2.http.GET


interface NewsApi {
    @GET("top-headlines?country=us&apiKey=${ApiConstants.API_KEY}")
    fun getNews(): Call<NewsResponse>
}
