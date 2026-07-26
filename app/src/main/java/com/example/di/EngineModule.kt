package com.example.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object EngineModule {
    // Empty for now, but ready for future explicit engine bindings if needed
}
