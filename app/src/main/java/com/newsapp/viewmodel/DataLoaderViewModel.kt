package com.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newsapp.entity.NewsResponse
import com.newsapp.service.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataLoaderViewModel : ViewModel() {

    private val _newsLiveData: MutableLiveData<NewsResponse> = MutableLiveData<NewsResponse>()
    val newsLiveData: LiveData<NewsResponse> = _newsLiveData
    private val networkService by lazy { NetworkService() }

    fun loadNewsList(query: String = "", category: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            _newsLiveData.postValue(networkService.loadNewsList(query = query, category = category))
        }
    }

}