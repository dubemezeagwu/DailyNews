package com.example.dailynews.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

        val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
        var searchNewsPage = 1

        init {
            getBreakingNews("NG")
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
                    return Resource.Success(it)
                }
            }
            return Resource.Error(response.message())
        }

        private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
            if (response.isSuccessful) {
                response.body()?.let {
                    return Resource.Success(it)
                }
            }
            return Resource.Error(response.message())
        }
}

