package com.example.dailynews.repository

import com.example.dailynews.database.ArticleDatabase
import com.example.dailynews.models.Article
import com.example.dailynews.models.NewsResponse
import com.example.dailynews.network.RetrofitInstance
import retrofit2.Response

class NewsRepository (
    val database: ArticleDatabase
    ) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse> {
        return RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)
    }

    suspend fun searchNews(searchQuery: String, pageNumber: Int) = RetrofitInstance.api.searchForAllNews(searchQuery,pageNumber)

    suspend fun upsert (article: Article) = database.getArticlesDao().upsert(article)

    fun getSavedNews () = database.getArticlesDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = database.getArticlesDao().deleteArticles(article)
}