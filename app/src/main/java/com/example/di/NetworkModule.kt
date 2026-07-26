package com.example.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // OkHttp/Gson are already in AppModule. We can add Retrofit here later if needed.
}
