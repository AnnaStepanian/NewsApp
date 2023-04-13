package com.newsapp.service

import com.newsapp.entity.NewsResponse

class NetworkService {
    private val retrofit by lazy { RetrofitHelper.getInstance() }
    fun loadNewsList(): NewsResponse {
        val apiService = retrofit.create(NewsApi::class.java)
        return apiService.getNews().execute().body() ?: throw Exception("news not found")
    }
}

