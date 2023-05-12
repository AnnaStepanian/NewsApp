import com.newsapp.entity.NewsResponse
import com.newsapp.service.NetworkService
import org.junit.Assert.assertNotNull
import org.junit.Test

class NetworkServiceTest {
    private val networkService = NetworkService()

    @Test
    fun testLoadNewsList() {
        val query = ""
        val category = "general"
        val newsResponse: NewsResponse = networkService.loadNewsList(query, category)

        assertNotNull(newsResponse.articles)
        newsResponse.articles?.isNotEmpty()?.let { assert(it) }
    }

    @Test
    fun testLoadNewsListWithQuery() {
        val query = "and"
        val category = "general"
        val newsResponse: NewsResponse = networkService.loadNewsList(query, category)

        assertNotNull(newsResponse.articles)
        newsResponse.articles?.forEach { article ->
            val titleContainsQuery = article.title?.contains(query, ignoreCase = true) == true
            val descContainsQuery = article.description?.contains(query, ignoreCase = true) == true
            assert(titleContainsQuery || descContainsQuery)
        }
    }
}
