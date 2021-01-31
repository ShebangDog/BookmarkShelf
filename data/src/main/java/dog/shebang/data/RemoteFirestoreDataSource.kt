package dog.shebang.data

import dog.shebang.data.firestore.BookmarkFirestore
import dog.shebang.model.Bookmark
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RemoteFirestoreDataSource {

    fun fetchBookmark(): Flow<Bookmark>

    suspend fun storeBookmark(bookmark: Bookmark)
}

class RemoteFirestoreDataSourceImpl @Inject constructor(
    private val bookmarkFirestore: BookmarkFirestore
) : RemoteFirestoreDataSource {

    override fun fetchBookmark(): Flow<Bookmark> = bookmarkFirestore.fetchBookmark()

    override suspend fun storeBookmark(bookmark: Bookmark) {
        bookmarkFirestore.storeBookmark(bookmark)
    }
}
