package com.example.dailynews.network

import com.example.dailynews.models.NewsResponse
import com.example.dailynews.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// * This interface is used to define our single request we will execute from code.
interface NewsAPI {
    @GET("v2/top-headlines")
    suspend fun  getBreakingNews(
        @Query("country")
        countryCode: String = "US",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ) : Response<NewsResponse>

    @GET("v2/everything")
    suspend fun  searchForAllNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ) : Response<NewsResponse>
}