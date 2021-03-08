package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.api.TwitterApi
import dog.shebang.data.api.TwitterApiClient
import dog.shebang.data.api.TwitterApiClientImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TwitterApiClientModule {

    @Singleton
    @Binds
    abstract fun bindTwitterApiClient(
        twitterApiClientImpl: TwitterApiClientImpl
    ): TwitterApiClient

}

@Module
@InstallIn(SingletonComponent::class)
object TwitterApiModule {

    private const val baseUrl = "https://api.twitter.com"

    @Singleton
    @Provides
    fun provideTwitterApi(): TwitterApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
        .create(TwitterApi::class.java)

}

