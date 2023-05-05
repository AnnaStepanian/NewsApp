package com.newsapp.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsapp.R
import com.newsapp.databinding.FragmentNewsBinding
import com.newsapp.viewmodel.DataLoaderViewModel
import com.newsapp.adapter.NewsAdapter
import com.newsapp.entity.ArticleResponse
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private val viewModel: DataLoaderViewModel by viewModels()
    var query: String = ""
    var category: String = ""
    var articles: List<ArticleResponse>? = null
    private var selectedCategoryId = R.id.category_general

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.searchButton.setOnClickListener {
            query = binding.searchBox.text.toString()
            try {
                viewModel.loadNewsList(query = query, category = category)
            } catch (e: Exception) {
                articles = articles?.let { it1 -> filterArticles(it1, query) }
                if (articles?.isEmpty() == true) {
                    binding.noResultsTextView.visibility = View.VISIBLE
                }
                binding.newsRecyclerView.adapter = articles?.let { NewsAdapter(it) }
            }
//            if (!isOnline(rxequireContext())) {
//                articles = articles?.let { it1 -> filterArticles(it1, query) }
//                if (articles?.isEmpty() == true) {
//                    binding.noResultsTextView.visibility = View.VISIBLE
//                }
//                binding.newsRecyclerView.adapter = articles?.let { NewsAdapter(it) }
//            } else {
//                viewModel.loadNewsList(query = query, category = category)
//            }
        }

        binding.filterButton.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.menuInflater.inflate(R.menu.filter_menu, popupMenu.menu)
            popupMenu.menu.findItem(selectedCategoryId)?.isChecked = true
            popupMenu.setOnMenuItemClickListener { menuItem ->
                val category = when (menuItem.itemId) {
                    R.id.category_business -> "business"
                    R.id.category_entertainment -> "entertainment"
                    R.id.category_general -> "general"
                    R.id.category_health -> "health"
                    else -> ""
                }
                this.category = menuItem.toString()
                selectedCategoryId = menuItem.itemId
                menuItem.isChecked = true

                // Apply the filter and dismiss the pop-up menu
                viewModel.loadNewsList(query=this.query, category=category)
                true
            }

            // Show the pop-up menu
            popupMenu.show()
        }

        viewModel.newsLiveData.observe(viewLifecycleOwner) { newsResponse ->
            articles = newsResponse.articles
            binding.newsRecyclerView.adapter = articles?.let { NewsAdapter(it) }
            binding.noResultsTextView.visibility = if (articles?.isEmpty() == true) View.VISIBLE else View.GONE
        }
        try {
            viewModel.loadNewsList()
        } catch (e: Exception) {
            binding.noResultsTextView.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.newsRecyclerView.layoutManager = layoutManager
        binding.newsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                layoutManager.orientation
            )
        )
    }

    fun filterArticles(articles: List<ArticleResponse>, query: String): List<ArticleResponse> {
        return articles.filter { article ->
            article.title?.contains(query, ignoreCase = true) == true || article.description?.contains(query, ignoreCase = true) == true
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

}
