package com.newsapp.service

import com.newsapp.entity.NewsResponse

class NetworkService {
    private val retrofit by lazy { RetrofitHelper.getInstance() }
    fun loadNewsList(query: String, category: String): NewsResponse {
        val apiService = retrofit.create(NewsApi::class.java)
        return apiService.getNews(query = query, category = category).execute().body()
            ?: throw Exception("news not found")
    }
}

