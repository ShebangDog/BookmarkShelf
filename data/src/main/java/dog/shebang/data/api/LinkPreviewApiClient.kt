package dog.shebang.data.api

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dog.shebang.data.di.LinkPreviewApiKey
import dog.shebang.model.Metadata
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

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
@InstallIn(ActivityComponent::class)
abstract class LinkPreviewApiModule {

    @Binds
    abstract fun bindLinkPreviewApiClient(
        linkPreviewApiClientImpl: LinkPreviewApiClientImpl
    ): LinkPreviewApiClient

}

@Module
@InstallIn(ActivityComponent::class)
object LinkPreviewApiClientModule {

    private const val baseUrl = "https://api.linkpreview.net/"

    @Provides
    fun provideLinkPreviewApi(): LinkPreviewApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
        .create(LinkPreviewApi::class.java)

}
