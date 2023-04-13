package com.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsapp.databinding.ItemNewsBinding
import com.newsapp.entity.ArticleResponse
import com.bumptech.glide.Glide

class NewsAdapter(private val articles: List<ArticleResponse>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    inner class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleResponse) {
            binding.article = article
            binding.titleTextView.text = article.title
            binding.descriptionTextView.text = article.description
            binding.authorTextView.text = article.author
            // Load the image using a library like Glide or Picasso
            // For example, using Glide:
            Glide.with(itemView)
                .load(article.urlToImage)
                .into(binding.imageView)
            binding.executePendingBindings()
        }
    }
}
