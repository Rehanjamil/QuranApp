package com.quran.rabiulqaloob.di

import android.app.Application
import android.content.Context
import com.quran.rabiulqaloob.utils.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreference(
        context: Application
    ): PreferenceHelper {
        return PreferenceHelper(context)
    }
}