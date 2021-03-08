package dog.shebang.data.firestore

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.ktx.auth
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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface TwitterBookmarkFirestore {

    fun fetchBookmarkList(): Flow<Result<List<Bookmark.TwitterBookmark>>>

    suspend fun storeBookmark(bookmark: Bookmark.TwitterBookmark)
}

class TwitterBookmarkFirestoreImpl @Inject constructor() : TwitterBookmarkFirestore {

    private val firestore = Firebase.firestore

    @ExperimentalCoroutinesApi
    override fun fetchBookmarkList(): Flow<Result<List<Bookmark.TwitterBookmark>>> = callbackFlow {
        val listenerRegistration = try {
            val uid = Firebase.auth.currentUser?.uid ?: throw FirebaseNotLoggedException

            firestore.twitterBookmarksRef(uid)
                .addSnapshotListener { snapshot, exception ->
                    exception?.run { throw this }

                    val entityList = snapshot?.toObjects(TwitterBookmarkEntity::class.java)
                    Log.d(TAG, "fetchBookmarkList Twitter: $entityList")

                    val bookmarkList = entityList
                        ?.mapNotNull { it.modelOrNull() }
                        ?: emptyList()

                    Log.d(TAG, "fetchBookmarkList Twitter2: $bookmarkList")
                    offer(Result.Success(bookmarkList))

                }

        } catch (throwable: Throwable) {

            offer(Result.Failure<Nothing>(throwable))
            null
        }

        awaitClose { listenerRegistration?.remove() }
    }

    @ExperimentalCoroutinesApi
    override suspend fun storeBookmark(bookmark: Bookmark.TwitterBookmark) {
        val uid = FirebaseAuthentication.currentUser.filterNotNull().first().uid

        firestore.twitterBookmarksRef(uid).add(bookmark)
    }

}

