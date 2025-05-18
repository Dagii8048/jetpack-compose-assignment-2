package com.example.jetpack_compose_assignment_2.di

import android.content.Context
import com.example.jetpack_compose_assignment_2.data.local.TodoDatabase
import com.example.jetpack_compose_assignment_2.data.remote.TodoApi
import com.example.jetpack_compose_assignment_2.data.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(
        @ApplicationContext context: Context
    ): TodoDatabase {
        return TodoDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideTodoApi(): TodoApi {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTodoRepository(
        api: TodoApi,
        db: TodoDatabase
    ): TodoRepository {
        return TodoRepository(api, db.todoDao())
    }
} 