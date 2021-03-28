package dog.shebang.shelf

import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dog.shebang.data.repository.BookmarkRepository
import dog.shebang.model.Bookmark
import dog.shebang.model.Category
import dog.shebang.model.LoadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

@Suppress("UNCHECKED_CAST")
class ShelfViewModel @AssistedInject constructor(
    private val bookmarkRepository: BookmarkRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
    @Assisted val category: Category
) : ViewModel() {

    data class UiModel(
        val isLoading: Boolean = true,
        val error: Throwable? = null,
        val bookmarkList: List<Bookmark> = emptyList(),
    )

    private val filteredBookmarkListFlow = bookmarkListFlow()
        .filterByCategory {
            if (category.isDefault()) true
            else it == category
        }

    val uiModel = filteredBookmarkListFlow.map { loadState ->
        val isLoading = loadState is LoadState.Loading

        try {
            UiModel(
                isLoading = isLoading,
                bookmarkList = (loadState as LoadState.Loaded).value
            )
        } catch (throwable: Throwable) {
            UiModel(
                false,
                throwable,
            )
        }
    }.onStart { emit(UiModel()) }.asLiveData()

    private fun bookmarkListFlow() = bookmarkRepository.fetchBookmarkList()
    private fun Flow<LoadState<List<Bookmark>>>.filterByCategory(
        predicate: (Category) -> Boolean
    ) = this.map { loadState ->
        loadState.map { bookmarkList ->
            bookmarkList.filter { bookmark ->
                val category = when (bookmark) {
                    is Bookmark.DefaultBookmark -> bookmark.category
                    is Bookmark.TwitterBookmark -> bookmark.category
                }

                predicate(category)
            }
        }
    }

    @AssistedFactory
    interface ShelfViewModelFactory {

        fun create(
            savedStateHandle: SavedStateHandle,
            category: Category
        ): ShelfViewModel
    }

    companion object {
        fun provideFactory(
            owner: SavedStateRegistryOwner,
            assistedFactory: ShelfViewModelFactory,
            category: Category
        ): ViewModelProvider.Factory = object : AbstractSavedStateViewModelFactory(owner, null) {

            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {

                return assistedFactory.create(handle, category) as T
            }
        }
    }
}