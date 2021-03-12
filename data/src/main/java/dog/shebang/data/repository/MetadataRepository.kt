package dog.shebang.data.repository

import dog.shebang.data.datasource.remote.RemoteMetadataDataSource
import dog.shebang.data.datasource.remote.RemoteTwitterMetadataDataSource
import dog.shebang.model.LoadState
import dog.shebang.model.Metadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

interface MetadataRepository {

    fun fetchMetadata(url: String): Flow<LoadState<Metadata>>
}

@Singleton
class DefaultMetadataRepository @Inject constructor(
    private val remoteTwitterMetadataDataSource: RemoteTwitterMetadataDataSource,
    private val remoteMetadataDataSource: RemoteMetadataDataSource
) : MetadataRepository {

    override fun fetchMetadata(url: String): Flow<LoadState<Metadata>> = flow {

        val metadataFlow = when (Metadata.Type.parseUrl(url)) {
            is Metadata.Type.Default -> remoteMetadataDataSource.fetchMetadata(url)
            is Metadata.Type.Twitter -> remoteTwitterMetadataDataSource.fetchMetadata(url)
        }

        emitAll(metadataFlow)
    }.onStart { emit(LoadState.Loading) }
}
