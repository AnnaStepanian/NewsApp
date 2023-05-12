package com.newsapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.newsapp.databinding.NewsDetailBinding
import com.newsapp.entity.ArticleResponse

class NewsDetailActivity : AppCompatActivity() {
    private lateinit var binding: NewsDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val articleJson = intent.getStringExtra("article")
        val article = Gson().fromJson(articleJson, ArticleResponse::class.java)

        println(article.title)

        binding.source.text = "Source: ${article.source?.name}"
        Glide.with(binding.imageView)
            .load(article.urlToImage)
            .into(binding.imageView)
        binding.author.text = "Author: ${article.author}"
        binding.title.text = article.title
        binding.description.text = article.description
    }
}