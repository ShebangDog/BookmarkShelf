package dog.shebang.data

import dog.shebang.data.firestore.BookmarkFirestore
import dog.shebang.model.Bookmark
import dog.shebang.model.LoadState
import dog.shebang.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface RemoteFirestoreDataSource {

    fun fetchBookmarkList(): Flow<LoadState<List<Bookmark>>>

    suspend fun storeBookmark(bookmark: Bookmark)
}

class RemoteFirestoreDataSourceImpl @Inject constructor(
    private val bookmarkFirestore: BookmarkFirestore
) : RemoteFirestoreDataSource {

    override fun fetchBookmarkList(): Flow<LoadState<List<Bookmark>>> = flow {
        emit(LoadState.Loading)

        val loadStateFlow = bookmarkFirestore.fetchBookmarkList().map { result ->
            when (result) {
                is Result.Failure -> LoadState.Error(result.throwable)
                is Result.Success -> LoadState.Loaded(result.value)
            }
        }

        emitAll(loadStateFlow)
    }

    override suspend fun storeBookmark(bookmark: Bookmark) {
        bookmarkFirestore.storeBookmark(bookmark)
    }
}
