package com.example.dailynews.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.dailynews.R
import com.example.dailynews.databinding.ActivityNewsBinding
import kotlinx.android.synthetic.main.activity_news.view.*

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sets NavController on the navHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)
//        binding.bottomNavigationView.setupWithNavController(binding.navHostFragment.findNavController())

    }
}