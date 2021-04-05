package dog.shebang.shelf

import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dog.shebang.data.auth.AuthViewModelDelegate
import dog.shebang.data.repository.BookmarkRepository
import dog.shebang.model.Bookmark
import dog.shebang.model.Category
import dog.shebang.model.LoadState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class ShelfViewModel @AssistedInject constructor(
    private val bookmarkRepository: BookmarkRepository,
    authViewModelDelegate: AuthViewModelDelegate,
    @Assisted private val savedStateHandle: SavedStateHandle,
    @Assisted val category: Category
) : ViewModel(), AuthViewModelDelegate by authViewModelDelegate {

    data class UiModel(
        val isLoading: Boolean = true,
        val error: Throwable? = null,
        val bookmarkList: List<Bookmark> = emptyList(),
    )

    val currentUserState = userInfoFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun updateUserData(uid: String?) = viewModelScope.launch {
        mutableBookmarkListFlow.emitAll(bookmarkListFlow(uid))
    }

    private val mutableBookmarkListFlow =
        MutableStateFlow<LoadState<List<Bookmark>>>(LoadState.Loading)

    private val filteredBookmarkListFlow = mutableBookmarkListFlow
        .filterByCategory {
            if (category.isDefault()) true
            else it == category
        }

    val uiModel = filteredBookmarkListFlow.map { loadState ->
        try {
            if (loadState is LoadState.Error) throw loadState.throwable

            val isLoading = loadState is LoadState.Loading
            val bookmarkList = if (loadState is LoadState.Loaded) loadState.value else null

            UiModel(
                isLoading = isLoading,
                bookmarkList = bookmarkList.orEmpty()
            )
        } catch (throwable: Throwable) {
            UiModel(
                isLoading = false,
                error = throwable,
            )
        }
    }.onStart { emit(UiModel()) }.asLiveData()

    private fun bookmarkListFlow(uid: String?) = bookmarkRepository.fetchBookmarkList(uid)

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