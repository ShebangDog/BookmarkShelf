package dog.shebang.data.repository

import dog.shebang.data.firestore.DefaultBookmarkFirestore
import dog.shebang.data.firestore.TwitterBookmarkFirestore
import dog.shebang.model.Bookmark
import dog.shebang.model.LoadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

interface BookmarkRepository {

    fun fetchBookmarkList(): Flow<LoadState<List<Bookmark>>>

    suspend fun storeBookmark(bookmark: Bookmark)
}

class DefaultBookmarkRepository @Inject constructor(
    private val defaultBookmarkFirestore: DefaultBookmarkFirestore,
    private val twitterBookmarkFirestore: TwitterBookmarkFirestore
) : BookmarkRepository {

    override fun fetchBookmarkList(): Flow<LoadState<List<Bookmark>>> {
        defaultBookmarkFirestore.fetchBookmarkList()

        return combine(
            defaultBookmarkFirestore.fetchBookmarkList(),
            twitterBookmarkFirestore.fetchBookmarkList()
        ) { defaultBookmark, twitterBookmark ->

            val defaultLoadState = defaultBookmark.toLoadState()
            val twitterLoadState = twitterBookmark.toLoadState()

            LoadState.map(defaultLoadState, twitterLoadState) { defaultList, twitterList ->
                defaultList + twitterList
            }
        }
    }

    override suspend fun storeBookmark(bookmark: Bookmark) {

        when (bookmark) {
            is Bookmark.DefaultBookmark -> defaultBookmarkFirestore.storeBookmark(bookmark)
            is Bookmark.TwitterBookmark -> twitterBookmarkFirestore.storeBookmark(bookmark)
        }
    }
}
