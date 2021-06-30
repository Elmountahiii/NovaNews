package com.redgunner.novanews.di

import android.content.Context
import com.redgunner.novanews.R
import com.redgunner.novanews.database.builder.LocalPostsDatabaseBuilder
import com.redgunner.novanews.database.dao.SavedPostsDAO
import com.redgunner.novanews.network.WordpressApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideWordPressLink(@ApplicationContext context: Context)=context.getString(R.string.WordpressLink)



    @Provides
    @Singleton
    fun  provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient,wordpressLink:String): Retrofit =
        Retrofit.Builder().baseUrl(wordpressLink)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()


    @Provides
    @Singleton
    fun provideWordpressApi(retrofit: Retrofit): WordpressApi =retrofit.create(WordpressApi::class.java)


    @Singleton
    @Provides
    fun  provideLocalDatabase(@ApplicationContext context: Context): LocalPostsDatabaseBuilder {
        return LocalPostsDatabaseBuilder.invoke(context)

    }


    @Singleton
    @Provides
    fun provideSavedPostsDao(localAccountDatabaseBuilder: LocalPostsDatabaseBuilder): SavedPostsDAO {
        return localAccountDatabaseBuilder.savedPostsDao()
    }





}