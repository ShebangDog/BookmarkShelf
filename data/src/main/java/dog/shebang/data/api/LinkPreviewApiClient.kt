package dog.shebang.data.api

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.di.LinkPreviewApiKey
import dog.shebang.model.Metadata
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

interface LinkPreviewApiClient {
    suspend fun fetchMetadata(url: String): Metadata
}

class LinkPreviewApiClientImpl @Inject constructor(
    private val linkPreviewApi: LinkPreviewApi,
    private val linkPreviewApiKey: LinkPreviewApiKey
) : LinkPreviewApiClient {

    override suspend fun fetchMetadata(url: String): Metadata =
        linkPreviewApi.fetchMetadata(linkPreviewApiKey.apiKey, url).toModel()

}

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
