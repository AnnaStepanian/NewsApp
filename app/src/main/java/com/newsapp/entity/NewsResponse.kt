package com.newsapp.entity

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("totalResults")
    val totalResults: Int? = null,
    @SerializedName("articles")
    var articles: List<ArticleResponse>? = null
): Cloneable {
    public override fun clone(): NewsResponse {
        return NewsResponse(
            status = status,
            totalResults = totalResults,
            articles = articles
        )
    }
}