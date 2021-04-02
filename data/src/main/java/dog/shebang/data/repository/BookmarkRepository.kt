package dog.shebang.data.repository

import android.util.Log
import dog.shebang.data.firestore.DefaultBookmarkFirestore
import dog.shebang.data.firestore.FirebaseNotLoggedException
import dog.shebang.data.firestore.TwitterBookmarkFirestore
import dog.shebang.model.Bookmark
import dog.shebang.model.LoadState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

interface BookmarkRepository {

    fun fetchBookmarkList(uid: String?): Flow<LoadState<List<Bookmark>>>

    suspend fun storeBookmark(uid: String?, bookmark: Bookmark)
}

class DefaultBookmarkRepository @Inject constructor(
    private val defaultBookmarkFirestore: DefaultBookmarkFirestore,
    private val twitterBookmarkFirestore: TwitterBookmarkFirestore
) : BookmarkRepository {

    override fun fetchBookmarkList(uid: String?) = flow {
        Log.d("AYUMU", "fetchBookmarkList: uid: $uid")

        if (uid == null) {
            emit(LoadState.Error(FirebaseNotLoggedException))
            return@flow
        }

        emitAll(
            combine(
                defaultBookmarkFirestore.fetchBookmarkList(uid),
                twitterBookmarkFirestore.fetchBookmarkList(uid)
            ) { defaultBookmark, twitterBookmark ->

                val defaultLoadState = defaultBookmark.toLoadState()
                val twitterLoadState = twitterBookmark.toLoadState()

                LoadState.map(defaultLoadState, twitterLoadState) { defaultList, twitterList ->
                    defaultList + twitterList
                }
            }
        )
    }.onStart { emit(LoadState.Loading) }

    override suspend fun storeBookmark(uid: String?, bookmark: Bookmark) {
        uid ?: return

        when (bookmark) {
            is Bookmark.DefaultBookmark -> defaultBookmarkFirestore.storeBookmark(uid, bookmark)
            is Bookmark.TwitterBookmark -> twitterBookmarkFirestore.storeBookmark(uid, bookmark)
        }
    }
}
