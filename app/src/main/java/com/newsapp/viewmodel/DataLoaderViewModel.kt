package com.newsapp.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.newsapp.entity.ArticleResponse
import com.newsapp.entity.NewsResponse
import com.newsapp.service.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DataLoaderViewModel : ViewModel() {

    private val _newsLiveData: MutableLiveData<NewsResponse?> = MutableLiveData<NewsResponse?>()
    val newsLiveData: MutableLiveData<NewsResponse?> = _newsLiveData
    val categoryToNewsResponseCache = mutableMapOf<String, NewsResponse>()
    private val networkService by lazy { NetworkService() }

    fun filterArticles(articles: List<ArticleResponse>?, query: String): List<ArticleResponse>? {
        return articles?.filter { article ->
            article.title?.contains(query, ignoreCase = true) == true || article.description?.contains(query, ignoreCase = true) == true
        }
    }

    fun loadNewsList(swipeRefreshLayout: SwipeRefreshLayout, query: String = "", category: String = "general") {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                swipeRefreshLayout.isRefreshing = true
            }

            try {
                val newsResponse = networkService.loadNewsList(query = query, category = category)
                if (query == "") {
                    categoryToNewsResponseCache[category] = newsResponse.clone()
                }
                _newsLiveData.postValue(newsResponse)
            } catch (e: Exception) {
                println(categoryToNewsResponseCache.containsKey(category))
                if (categoryToNewsResponseCache.containsKey(category)) {
                    val newsResponse = categoryToNewsResponseCache[category]?.clone()
                    newsResponse?.articles = filterArticles(newsResponse?.articles, query)
                    _newsLiveData.postValue(newsResponse)
                }
            }

            withContext(Dispatchers.Main) {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

}