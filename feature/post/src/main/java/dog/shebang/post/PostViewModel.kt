package dog.shebang.post

import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dog.shebang.data.repository.BookmarkRepository
import dog.shebang.data.repository.CategoryRepository
import dog.shebang.data.repository.MetadataRepository
import dog.shebang.model.Bookmark
import dog.shebang.model.Category
import dog.shebang.model.LoadState
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class PostViewModel @AssistedInject constructor(
    private val metadataRepository: MetadataRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val categoryRepository: CategoryRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
    @Assisted private val url: String?
) : ViewModel() {

    val metadataLiveData = liveData {
        if (url == null) {
            emit(LoadState.Error(NullPointerException("url is null")))
            return@liveData
        }

        emitSource(metadataRepository.fetchMetadata(url).asLiveData())
    }

    private val mutableCategoryLiveData = MutableLiveData<Category>()
    val categoryLiveData: LiveData<Category> = mutableCategoryLiveData

    fun storeBookmark(bookmark: Bookmark) = viewModelScope.launch {
        bookmarkRepository.storeBookmark(bookmark)
    }

    fun saveCategory(category: Category) = viewModelScope.launch {
        categoryRepository.saveCategory(category)
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