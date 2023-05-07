package com.newsapp.view

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.newsapp.R
import com.newsapp.adapter.NewsAdapter
import com.newsapp.databinding.FragmentNewsBinding
import com.newsapp.entity.ArticleResponse
import com.newsapp.viewmodel.DataLoaderViewModel


class NewsFragment : Fragment() {

    private lateinit var binding: FragmentNewsBinding
    private val viewModel: DataLoaderViewModel by viewModels()
    private var query: String = ""
    private var category: String = "general"
    private var articles: List<ArticleResponse>? = null
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
        binding.textBoxLayout.setEndIconOnClickListener {
            query = ""
            viewModel.loadNewsList(binding.swipeRefreshLayout, query = query, category = category)
            binding.textBoxLayout.editText?.apply {
                text?.clear()
                clearFocus()
                // Hide the keyboard
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(windowToken, 0)
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadNewsList(binding.swipeRefreshLayout, query = query, category = category)
        }
        binding.searchButton.setOnClickListener {
            query = binding.searchBox.text.toString()
            try {
                viewModel.loadNewsList(binding.swipeRefreshLayout, query = query, category = category)
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
                viewModel.loadNewsList(binding.swipeRefreshLayout, query=this.query, category=category)
                true
            }

            // Show the pop-up menu
            popupMenu.show()
        }

        val listener = object : NewsAdapter.OnItemClickListener {
            override fun onItemClick(article: ArticleResponse) {
                val intent = Intent(requireContext(), NewsDetailActivity::class.java)
                val articleJson = Gson().toJson(article)
                intent.putExtra("article", articleJson)
                startActivity(intent)
            }
        }
        viewModel.newsLiveData.observe(viewLifecycleOwner) { newsResponse ->
            articles = newsResponse?.articles

            val adapter = articles?.let { NewsAdapter(it) }
            adapter?.setOnItemClickListener(listener)

            binding.newsRecyclerView.adapter = adapter
            binding.noResultsTextView.visibility = if (articles?.isEmpty() == true) View.VISIBLE else View.GONE
        }

        val adapter = NewsAdapter(articles ?: listOf())
        adapter.setOnItemClickListener(listener)
        binding.newsRecyclerView.adapter = adapter

        try {
            viewModel.loadNewsList(binding.swipeRefreshLayout)
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

    private fun filterArticles(articles: List<ArticleResponse>, query: String): List<ArticleResponse> {
        return articles.filter { article ->
            article.title?.contains(query, ignoreCase = true) == true || article.description?.contains(query, ignoreCase = true) == true
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
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
        }
        return false
    }

}
