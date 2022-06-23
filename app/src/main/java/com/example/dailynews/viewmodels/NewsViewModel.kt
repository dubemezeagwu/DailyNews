package com.example.dailynews.viewmodels

import androidx.lifecycle.ViewModel
import com.example.dailynews.repository.NewsRepository

class NewsViewModel ( val newsRepository: NewsRepository) : ViewModel() {
}