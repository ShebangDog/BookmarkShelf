package dog.shebang.data.datasource.remote

import dog.shebang.data.firestore.DefaultBookmarkFirestore
import dog.shebang.model.Bookmark
import dog.shebang.model.LoadState
import dog.shebang.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface FirestoreDefaultBookmarkDataSource {

    fun fetchBookmarkList(): Flow<LoadState<List<Bookmark.DefaultBookmark>>>

    suspend fun storeBookmark(bookmark: Bookmark.DefaultBookmark)
}

class FirestoreDefaultBookmarkDataSourceImpl @Inject constructor(
    private val defaultBookmarkFirestore: DefaultBookmarkFirestore
) : FirestoreDefaultBookmarkDataSource {

    override fun fetchBookmarkList(): Flow<LoadState<List<Bookmark.DefaultBookmark>>> = flow {
        emit(LoadState.Loading)

        val loadStateFlow = defaultBookmarkFirestore.fetchBookmarkList().map { result ->
            when (result) {
                is Result.Failure -> LoadState.Error(result.throwable)
                is Result.Success -> LoadState.Loaded(result.value)
            }
        }

        emitAll(loadStateFlow)
    }

    override suspend fun storeBookmark(bookmark: Bookmark.DefaultBookmark) {
        defaultBookmarkFirestore.storeBookmark(bookmark)
    }
}
