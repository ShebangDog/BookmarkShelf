package dog.shebang.data.api

import dog.shebang.env.Environment
import dog.shebang.model.Metadata
import dog.shebang.model.Result
import javax.inject.Inject

interface LinkPreviewApiClient {
    suspend fun fetchMetadata(url: String): Result<Metadata>
}

class LinkPreviewApiClientImpl @Inject constructor(
    private val linkPreviewApi: LinkPreviewApi,
    private val linkPreviewApiKey: LinkPreviewApiKey
) : LinkPreviewApiClient {

    override suspend fun fetchMetadata(url: String): Result<Metadata> {
        val response = linkPreviewApi.fetchMetadata(linkPreviewApiKey.apiKey, url)

        return try {
            Result.Success(response.body()!!.toModel())
        } catch (throwable: Throwable) {
            Result.Failure(throwable)
        }
    }

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