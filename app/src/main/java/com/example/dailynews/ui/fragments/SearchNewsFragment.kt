package com.example.dailynews.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailynews.R
import com.example.dailynews.adapters.NewsAdapter
import com.example.dailynews.ui.activities.NewsActivity
import com.example.dailynews.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.dailynews.utils.Resource
import com.example.dailynews.viewmodels.NewsViewModel
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    val TAG = "SearchNewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        var job: Job? = null
        searchNews_search.addTextChangedListener{
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                it?.let {
                    if (it.toString().isNotEmpty()){
                        viewModel.searchNews(it.toString())
                    }
                }
            }
        }
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.e(TAG, "An Error Occurred: $it")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }

    private fun setupRecyclerView (){
        newsAdapter = NewsAdapter()
        searchNews_recycler_view.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar (){
        searchNews_progress_bar.visibility = View.INVISIBLE
    }

    private fun showProgressBar (){
        searchNews_progress_bar.visibility = View.VISIBLE
    }

}