package dog.shebang.data

import dog.shebang.data.firestore.BookmarkFirestore
import dog.shebang.model.Bookmark
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RemoteFirestoreDataSource {

    fun fetchBookmarkList(): Flow<List<Bookmark>>

    suspend fun storeBookmark(bookmark: Bookmark)
}

class RemoteFirestoreDataSourceImpl @Inject constructor(
    private val bookmarkFirestore: BookmarkFirestore
) : RemoteFirestoreDataSource {

    override fun fetchBookmarkList(): Flow<List<Bookmark>> = bookmarkFirestore.fetchBookmarkList()

    override suspend fun storeBookmark(bookmark: Bookmark) {
        bookmarkFirestore.storeBookmark(bookmark)
    }
}
