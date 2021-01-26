package dog.shebang.data.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dog.shebang.env.Environment
import dog.shebang.model.Metadata
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

interface LinkPreviewApiClient {
    suspend fun fetchMetadata(url: String): Metadata
}

class LinkPreviewApiClientImpl(
    private val linkPreviewApi: LinkPreviewApi,
    private val apiToken: String
) : LinkPreviewApiClient {

    override suspend fun fetchMetadata(url: String): Metadata =
        linkPreviewApi.fetchMetadata(apiToken, url).toModel()

}

@Module
@InstallIn(ActivityComponent::class)
class LinkPreviewApiModule {

    private val baseUrl = "https://api.linkpreview.net/"

    @Inject
    lateinit var environment: Environment

    private fun provideLinkPreviewApi(): LinkPreviewApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
        .create(LinkPreviewApi::class.java)

    @Provides
    fun provideLinkPreviewApiClient(): LinkPreviewApiClient = LinkPreviewApiClientImpl(
        provideLinkPreviewApi(), environment.linkPreviewApiKey
    )

}
