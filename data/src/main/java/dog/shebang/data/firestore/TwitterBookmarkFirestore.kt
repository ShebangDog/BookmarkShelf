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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

interface TwitterBookmarkFirestore {

    fun fetchBookmarkList(): Flow<Result<List<Bookmark.TwitterBookmark>>>

    suspend fun storeBookmark(bookmark: Bookmark.TwitterBookmark)
}

class TwitterBookmarkFirestoreImpl @Inject constructor() : TwitterBookmarkFirestore {

    private val firestore = Firebase.firestore

    @ExperimentalCoroutinesApi
    override fun fetchBookmarkList(): Flow<Result<List<Bookmark.TwitterBookmark>>> =
        FirebaseAuthentication.currentUser.flatMapLatest { user ->
            callbackFlow {
                val listenerRegistration = try {
                    val uid = user?.uid ?: throw FirebaseNotLoggedException

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
        }

    @ExperimentalCoroutinesApi
    override suspend fun storeBookmark(bookmark: Bookmark.TwitterBookmark) {
        val uid = FirebaseAuthentication.currentUser.firstOrNull()?.uid ?: return

        firestore.twitterBookmarksRef(uid).add(bookmark)
    }

}

