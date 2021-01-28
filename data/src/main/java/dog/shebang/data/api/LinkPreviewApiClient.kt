package dog.shebang.data.api

import dog.shebang.env.Environment
import dog.shebang.model.Metadata
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

interface LinkPreviewApiKey {

    val apiKey: String
}

class LinkPreviewApiKeyImpl @Inject constructor(
    private val environment: Environment
) : LinkPreviewApiKey {

    override val apiKey: String
        get() = environment.linkPreviewApiKey
}