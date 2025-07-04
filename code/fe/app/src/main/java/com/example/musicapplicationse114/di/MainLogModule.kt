package com.example.musicapplicationse114.di

import com.example.musicapplicationse114.repositories.MainLog
import com.example.musicapplicationse114.repositories.MainLogImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainLogModule {

    @Binds
    @Singleton
    abstract fun bindMainLog(log: MainLogImpl): MainLog
}