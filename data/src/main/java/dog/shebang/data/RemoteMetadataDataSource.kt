package dog.shebang.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dog.shebang.data.api.LinkPreviewApiClient
import dog.shebang.model.Metadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface RemoteMetadataDataSource {

    fun fetchMetadata(url: String): Flow<Metadata?>
}

class RemoteMetadataDataSourceImpl @Inject constructor(
    private val linkPreviewApiClient: LinkPreviewApiClient
) : RemoteMetadataDataSource {

    override fun fetchMetadata(url: String): Flow<Metadata?> = flow {
        try {
            emit(linkPreviewApiClient.fetchMetadata(url))
        } catch (exception: Exception) {
            emit(null)
        }
    }
}

@Module
@InstallIn(ActivityComponent::class)
abstract class RemoteMetadataDataSourceModule {

    @Binds
    abstract fun bindRemoteMetadataDataSource(
        remoteMetadataDataSourceImpl: RemoteMetadataDataSourceImpl
    ): RemoteMetadataDataSource

}