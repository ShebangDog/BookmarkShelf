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
import dog.shebang.model.Metadata
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class PostViewModel @AssistedInject constructor(
    private val metadataRepository: MetadataRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val categoryRepository: CategoryRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
    @Assisted private val url: String?
) : ViewModel() {

    data class UiModel(
        val isLoading: Boolean = true,
        val error: Throwable? = null,
        val defaultMetadata: Metadata.DefaultMetadata? = null,
        val twitterMetadata: Metadata.TwitterMetadata? = null,
        val category: Category = Category.defaultCategory,
    )

    private val mutableCategoryStateFlow = MutableStateFlow(Category.defaultCategory)
    private val categoryStateFlow = mutableCategoryStateFlow

    val uiModel = flow {
        try {
            url ?: throw NullPointerException("url is null")

            val uiModelFlow = combine(
                metadataRepository.fetchMetadata(url),
                categoryStateFlow
            ) { loadStateMetadata, category ->
                val isLoading = (loadStateMetadata is LoadState.Loading)

                val error = null
                val metadata =
                    if (loadStateMetadata is LoadState.Loaded) loadStateMetadata.value
                    else null

                UiModel(
                    isLoading = isLoading,
                    error = error,
                    category = category,
                    defaultMetadata = metadata?.orNull(),
                    twitterMetadata = metadata?.orNull(),
                )
            }

            emitAll(uiModelFlow)
        } catch (throwable: Throwable) {
            emit(UiModel(isLoading = false, error = throwable))
        }
    }
        .onStart { emit(UiModel()) }
        .asLiveData()

    fun storeBookmark(bookmark: Bookmark) = viewModelScope.launch {

        bookmarkRepository.storeBookmark(bookmark)
    }

    fun saveCategory(category: Category) = viewModelScope.launch {
        categoryRepository.saveCategory(category)
        mutableCategoryStateFlow.value = category
    }

    fun setCategory(category: Category) = viewModelScope.launch {
        mutableCategoryStateFlow.value = category
    }

    private inline fun <reified T : Metadata> Metadata.orNull(): T? = if (this is T) this else null

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
