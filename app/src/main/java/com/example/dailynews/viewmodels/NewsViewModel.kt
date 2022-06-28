package com.example.dailynews.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailynews.NewsApplication
import com.example.dailynews.models.Article
import com.example.dailynews.models.NewsResponse
import com.example.dailynews.repository.NewsRepository
import com.example.dailynews.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel (
    application: Application,
    val newsRepository: NewsRepository
    ) : AndroidViewModel(application) {
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
            safeBreakingNewsCall(countryCode)
        }

        fun searchNews (searchQuery: String) = viewModelScope.launch {
            safeSearchNewsCall(searchQuery)
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

    private suspend fun safeBreakingNewsCall (countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("Oops...No Internet Connection"))
            }

        } catch (t: Throwable){
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeSearchNewsCall (searchQuery: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRepository.searchNews(searchQuery,searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("Oops...No Internet Connection"))
            }

        } catch (t: Throwable){
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection (): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true

                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true

                    else -> false
                }
            }
        }
        return false
    }
}

