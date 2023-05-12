package com.newsapp.viewmodel

import com.newsapp.entity.ArticleResponse
import com.newsapp.entity.NewsResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class DataLoaderViewModelTest {

    private val viewModel = DataLoaderViewModel()

    @Test
    fun filterArticlesTest() {
        // Given a list of articles
        val articles = listOf(
            ArticleResponse(
                title = "Test article 1",
                description = "This is a test article."
            ),
            ArticleResponse(
                title = "Test article 2",
                description = "This is another test article."
            ),
            ArticleResponse(
                title = "Third article",
                description = "This article should not match."
            )
        )

        // When the filterArticles function is called with a query string
        val filteredArticles = viewModel.filterArticles(articles, "test")

        // Then only the articles containing the query string in either title or description should be returned
        assertEquals(2, filteredArticles?.size)
        assertEquals("Test article 1", filteredArticles?.get(0)?.title)
        assertEquals("This is a test article.", filteredArticles?.get(0)?.description)
        assertEquals("Test article 2", filteredArticles?.get(1)?.title)
        assertEquals("This is another test article.", filteredArticles?.get(1)?.description)
    }
}
