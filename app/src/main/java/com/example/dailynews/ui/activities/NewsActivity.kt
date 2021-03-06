package com.example.dailynews.ui.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.dailynews.R
import com.example.dailynews.adapters.NewsAdapter
import com.example.dailynews.database.ArticleDatabase
import com.example.dailynews.databinding.ActivityNewsBinding
import com.example.dailynews.repository.NewsRepository
import com.example.dailynews.viewmodels.NewsViewModel
import com.example.dailynews.viewmodels.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_news.view.*

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        var colorDrawable = ColorDrawable(Color.parseColor("#FFFFFFFF"))
        actionBar.setBackgroundDrawable(colorDrawable)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.elevation = 0F
        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar.setCustomView(R.layout.custom_action_bar)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        // Sets NavController on the navHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)
//        binding.bottomNavigationView.setupWithNavController(binding.navHostFragment.findNavController())
    }


}