package com.example.dailynews.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dailynews.models.Article

@Dao
interface ArticleDao {

    // * This function inserts or updates an article, onConflict helps to check if that article we
    // * we want to add in the database already exists, and replaces it.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun  deleteArticles (article: Article)
}
