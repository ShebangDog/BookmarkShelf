package dog.shebang.shelf

import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dog.shebang.data.RemoteFirestoreDataSource
import dog.shebang.model.Category
import kotlinx.coroutines.flow.map

@Suppress("UNCHECKED_CAST")
class ShelfViewModel @AssistedInject constructor(
    remoteFirestoreDataSource: RemoteFirestoreDataSource,
    @Assisted private val savedStateHandle: SavedStateHandle,
    @Assisted val category: Category
) : ViewModel() {

    private val isDefaultCategory = category.value == Category.defaultCategoryName

    val bookmarkListLiveData = remoteFirestoreDataSource.fetchBookmarkList()
        .let { bookmarkFlowList ->
            if (isDefaultCategory) bookmarkFlowList
            else bookmarkFlowList.map { bookmarkList -> bookmarkList.filter { it.category.value == category.value } }
        }.asLiveData()

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