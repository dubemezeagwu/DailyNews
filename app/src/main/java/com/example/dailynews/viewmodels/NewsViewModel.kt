package com.example.dailynews.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailynews.models.Article
import com.example.dailynews.models.NewsResponse
import com.example.dailynews.repository.NewsRepository
import com.example.dailynews.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel (
    val newsRepository: NewsRepository
    ) : ViewModel() {
        val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var breakingNewsPage = 1
        var breakingNewsResponse: NewsResponse? = null

        val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var searchNewsPage = 1
        var searchNewsResponse: NewsResponse? = null


    init {
            getBreakingNews("US")
        }

        fun getBreakingNews(countryCode: String) = viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
            val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
            breakingNews.postValue(handleBreakingNewsResponse(response))
        }

        fun searchNews (searchQuery: String) = viewModelScope.launch {
            searchNews.postValue(Resource.Loading())
            val response = newsRepository.searchNews(searchQuery, searchNewsPage)
            searchNews.postValue(handleSearchNewsResponse(response))
        }

        private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
            if (response.isSuccessful) {
                response.body()?.let {
                    breakingNewsPage++
                    if(breakingNewsResponse == null){
                        breakingNewsResponse = it
                    } else {
                        val oldArticles = breakingNewsResponse?.articles
                        val newArticles = it.articles
                        oldArticles?.addAll(newArticles)
                    }
                    return Resource.Success(breakingNewsResponse ?: it)
                }
            }
            return Resource.Error(response.message())
        }

        private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
            if (response.isSuccessful) {
                response.body()?.let {
                    searchNewsPage++
                    if(searchNewsResponse == null){
                        searchNewsResponse = it
                    } else {
                        val oldArticles = searchNewsResponse?.articles
                        val newArticles = it.articles
                        oldArticles?.addAll(newArticles)
                    }
                    return Resource.Success(searchNewsResponse ?: it)
                }
            }
            return Resource.Error(response.message())
        }

        fun saveArticle(article: Article) = viewModelScope.launch {
            newsRepository.upsert(article)
        }

        fun getSavedNews () = newsRepository.getSavedNews()

        fun deleteArticle(article: Article) = viewModelScope.launch {
            newsRepository.deleteArticle(article)
        }
}

