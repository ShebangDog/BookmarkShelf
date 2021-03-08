package dog.shebang.data.api

import dog.shebang.env.Environment
import dog.shebang.model.Result
import dog.shebang.model.Twitter
import javax.inject.Inject

interface TwitterApiClient {
    suspend fun fetchTweet(id: String): Result<Twitter.Tweet>

    suspend fun fetchUserData(id: String): Result<Twitter.UserData>
}

class TwitterApiClientImpl @Inject constructor(
    private val twitterApi: TwitterApi,
    private val apiKey: TwitterApiKey
) : TwitterApiClient {

    override suspend fun fetchTweet(id: String): Result<Twitter.Tweet> {
        val response = twitterApi.fetchTweet(
            bearerToken = apiKey.bearerToken,
            id = id,
            expansions = Expansions(listOf("attachments.media_keys")),
            tweetFields = TweetFields(listOf("author_id", "entities")),
            mediaFields = MediaFields(listOf("url"))
        )

        return try {
            Result.Success(response.body()?.toModel()!!)
        } catch (throwable: Throwable) {
            Result.Failure(throwable)
        }
    }

    override suspend fun fetchUserData(id: String): Result<Twitter.UserData> {
        val response = twitterApi.fetchUserData(
            bearerToken = apiKey.bearerToken,
            id = id,
            userFields = UserFields(listOf("profile_image_url"))
        )

        return try {
            Result.Success(response.body()!!.toModel())
        } catch (throwable: Throwable) {
            Result.Failure(throwable)
        }
    }
}

interface TwitterApiKey {

    val bearerToken: String
}

class TwitterApiKeyImpl @Inject constructor(
    environment: Environment
) : TwitterApiKey {

    override val bearerToken: String = "Bearer ${environment.twitterBearerToken}"
}
