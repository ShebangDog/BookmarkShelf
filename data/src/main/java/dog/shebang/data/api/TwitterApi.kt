package dog.shebang.data.api

import dog.shebang.data.api.entity.TwitterEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TwitterApi {

    @GET("/2/tweets/{id}")
    suspend fun fetchTweet(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: String,
        @Query("expansions") expansions: Expansions,
        @Query("tweet.fields") tweetFields: TweetFields,
        @Query("media.fields") mediaFields: MediaFields
    ): Response<TwitterEntity.Tweet>

    @GET("/2/users/{id}")
    suspend fun fetchUserData(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: String,
        @Query("user.fields") userFields: UserFields,
    ): Response<TwitterEntity.UserData>
}

class Expansions(private val values: List<String>) {

    override fun toString(): String {

        return values.joinToString(separator = ",")
    }
}


class TweetFields(private val values: List<String>) {

    override fun toString(): String {

        return values.joinToString(separator = ",")
    }
}

class UserFields(private val values: List<String>) {

    override fun toString(): String {

        return values.joinToString(separator = ",")
    }
}

class MediaFields(private val values: List<String>) {

    override fun toString(): String {

        return values.joinToString(separator = ",")
    }
}
