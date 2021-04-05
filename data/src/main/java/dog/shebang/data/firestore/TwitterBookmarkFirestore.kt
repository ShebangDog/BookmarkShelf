package dog.shebang.data.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dog.shebang.data.firestore.entity.TwitterBookmarkEntity
import dog.shebang.data.firestore.ext.twitterBookmarksRef
import dog.shebang.model.Bookmark
import dog.shebang.model.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

interface TwitterBookmarkFirestore {

    fun fetchBookmarkList(uid: String): Flow<Result<List<Bookmark.TwitterBookmark>>>

    suspend fun storeBookmark(uid: String, bookmark: Bookmark.TwitterBookmark)
}

class TwitterBookmarkFirestoreImpl @Inject constructor() : TwitterBookmarkFirestore {

    private val firestore = Firebase.firestore

    @ExperimentalCoroutinesApi
    override fun fetchBookmarkList(uid: String) = callbackFlow {
        val listenerRegistration = try {
            firestore.twitterBookmarksRef(uid)
                .addSnapshotListener { snapshot, exception ->
                    exception?.run { throw this }

                    val entityList =
                        snapshot?.toObjects(TwitterBookmarkEntity::class.java)
                    val bookmarkList = entityList
                        ?.mapNotNull { it.modelOrNull() }
                        ?: emptyList()

                    offer(Result.Success(bookmarkList))

                }

        } catch (throwable: Throwable) {

            offer(Result.Failure<Nothing>(throwable))
            null
        }

        awaitClose { listenerRegistration?.remove() }
    }


    @ExperimentalCoroutinesApi
    override suspend fun storeBookmark(uid: String, bookmark: Bookmark.TwitterBookmark) {
        firestore.twitterBookmarksRef(uid).add(bookmark)
    }

}

