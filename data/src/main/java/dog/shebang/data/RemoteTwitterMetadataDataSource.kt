package dog.shebang.data

import dog.shebang.data.api.TwitterApiClient
import dog.shebang.model.LoadState
import dog.shebang.model.Metadata
import dog.shebang.model.Twitter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface RemoteTwitterMetadataDataSource {

    fun fetchMetadata(url: String): Flow<LoadState<Metadata.TwitterMetadata>>
}

class RemoteTwitterMetadataDataSourceImpl @Inject constructor(
    private val twitterApiClient: TwitterApiClient
) : RemoteTwitterMetadataDataSource {

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun fetchMetadata(url: String): Flow<LoadState<Metadata.TwitterMetadata>> {
        val id = Metadata.Type.urlToTweetId(url)

        return combine(fetchTweet(id), fetchUserData(id)) { tweetState, userDataState ->
            val loadStateMetadata = LoadState.map(tweetState, userDataState) { tweet, userData ->

                Metadata.TwitterMetadata(
                    authorName = userData.data.name,
                    authorProfileUrl = userData.data.profileImageUrl,
                    text = tweet.data.text,
                    internal = Metadata.DefaultMetadata(
                        previewImageUrl = tweet.data.previews.firstOrNull()?.images?.firstOrNull()?.url,
                        description = tweet.data.previews.firstOrNull()?.description,
                        url = tweet.data.previews.firstOrNull()?.expanded_url ?: url,
                        title = tweet.data.previews.firstOrNull()?.title
                    )
                )
            }

            loadStateMetadata
        }

    }

    private fun fetchTweet(id: String): Flow<LoadState<Twitter.Tweet>> = flow {
        val loadState = twitterApiClient.fetchTweet(id).toLoadState()

        emit(loadState)
    }

    @FlowPreview
    private fun fetchUserData(id: String): Flow<LoadState<Twitter.UserData>> =
        fetchTweet(id).map {
            when (val loadState = it.map { tweet -> tweet.data.authorId }) {
                is LoadState.Error -> LoadState.Error(loadState.throwable)
                is LoadState.Loading -> LoadState.Loading
                is LoadState.Loaded -> twitterApiClient
                    .fetchUserData(loadState.value)
                    .toLoadState()
            }
        }

}