package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.api.LinkPreviewApi
import dog.shebang.data.api.LinkPreviewApiClient
import dog.shebang.data.api.LinkPreviewApiClientImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LinkPreviewApiModule {

    @Singleton
    @Binds
    abstract fun bindLinkPreviewApiClient(
        linkPreviewApiClientImpl: LinkPreviewApiClientImpl
    ): LinkPreviewApiClient

}

@Module
@InstallIn(SingletonComponent::class)
object LinkPreviewApiClientModule {

    private const val baseUrl = "https://api.linkpreview.net/"

    @Singleton
    @Provides
    fun provideLinkPreviewApi(): LinkPreviewApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
        .create(LinkPreviewApi::class.java)

}
