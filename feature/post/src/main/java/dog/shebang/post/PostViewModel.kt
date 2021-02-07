package dog.shebang.post

import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dog.shebang.data.RemoteCategoryDataSource
import dog.shebang.data.RemoteFirestoreDataSource
import dog.shebang.data.RemoteMetadataDataSource
import dog.shebang.model.Bookmark
import dog.shebang.model.Category
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class PostViewModel @AssistedInject constructor(
    private val remoteMetadataDataSource: RemoteMetadataDataSource,
    private val remoteFirestoreDataSource: RemoteFirestoreDataSource,
    private val remoteCategoryDataSource: RemoteCategoryDataSource,
    @Assisted private val savedStateHandle: SavedStateHandle,
    @Assisted private val url: String?
) : ViewModel() {

    val metadataLiveData = liveData {
        url ?: return@liveData

        emitSource(remoteMetadataDataSource.fetchMetadata(url).asLiveData())
    }

    private val mutableCategoryLiveData = MutableLiveData<Category>()
    val categoryLiveData: LiveData<Category> = mutableCategoryLiveData

    fun storeBookmark(bookmark: Bookmark) = viewModelScope.launch {
        remoteFirestoreDataSource.storeBookmark(bookmark)
    }

    fun saveCategory(category: Category) = viewModelScope.launch {
        remoteCategoryDataSource.saveCategory(category)
        mutableCategoryLiveData.value = category
    }

    fun setCategory(category: Category) = viewModelScope.launch {
        mutableCategoryLiveData.value = category
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