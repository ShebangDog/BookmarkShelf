package dog.shebang.post

import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dog.shebang.data.RemoteMetadataDataSource

@Suppress("UNCHECKED_CAST")
class PostViewModel @AssistedInject constructor(
    private val remoteMetadataDataSource: RemoteMetadataDataSource,
    @Assisted private val savedStateHandle: SavedStateHandle,
    @Assisted private val url: String?
) : ViewModel() {

    val metadataLiveData = liveData {
        url ?: return@liveData

        emitSource(remoteMetadataDataSource.fetchMetadata(url).asLiveData())
    }

    fun storeBookmark() {

    }

    @AssistedFactory
    interface PostViewModelFactory {
        fun create(
            savedStateHandle: SavedStateHandle,
            url: String?
        ): PostViewModel
    }

    companion object {
        fun provideFactory(
            owner: SavedStateRegistryOwner,
            assistedFactory: PostViewModelFactory,
            url: String?
        ): ViewModelProvider.Factory = object : AbstractSavedStateViewModelFactory(owner, null) {

            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {

                return assistedFactory.create(handle, url) as T
            }
        }
    }

}