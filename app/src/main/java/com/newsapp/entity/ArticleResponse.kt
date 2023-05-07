package com.newsapp.entity

import com.google.gson.annotations.SerializedName


data class ArticleResponse(
    @SerializedName("source")
    val source: SourceResponse? = null,
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("urlToImage")
    val urlToImage: String? = null,
    @SerializedName("publishedAt")
    val publishedAt: String? = null,
    @SerializedName("content")
    val content: String? = null
)