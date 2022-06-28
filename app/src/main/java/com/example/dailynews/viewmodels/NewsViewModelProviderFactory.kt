package com.example.dailynews.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dailynews.repository.NewsRepository

class NewsViewModelProviderFactory (
    val application: Application,
    private val newsRepository: NewsRepository
    ): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(application, newsRepository) as T
    }

}