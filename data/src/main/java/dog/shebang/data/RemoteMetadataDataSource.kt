package dog.shebang.data

import dog.shebang.data.api.LinkPreviewApiClient
import dog.shebang.model.LoadState
import dog.shebang.model.Metadata
import dog.shebang.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface RemoteMetadataDataSource {

    fun fetchMetadata(url: String): Flow<LoadState<Metadata.DefaultMetadata>>
}

class RemoteMetadataDataSourceImpl @Inject constructor(
    private val linkPreviewApiClient: LinkPreviewApiClient
) : RemoteMetadataDataSource {

    override fun fetchMetadata(url: String): Flow<LoadState<Metadata.DefaultMetadata>> = flow {
        emit(LoadState.Loading)
        try {
            val state = when (val result = linkPreviewApiClient.fetchMetadata(url)) {
                is Result.Success -> LoadState.Loaded(result.value)
                is Result.Failure -> LoadState.Error(result.throwable)
            }

            emit(state)
        } catch (throwable: Throwable) {
            emit(LoadState.Error(throwable))
        }

    }
}
