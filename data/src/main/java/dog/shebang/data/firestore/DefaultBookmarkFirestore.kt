package dog.shebang.data.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dog.shebang.data.firestore.entity.DefaultBookmarkEntity
import dog.shebang.data.firestore.ext.defaultBookmarksRef
import dog.shebang.model.Bookmark
import dog.shebang.model.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

interface DefaultBookmarkFirestore {

    fun fetchBookmarkList(uid: String): Flow<Result<List<Bookmark.DefaultBookmark>>>

    suspend fun storeBookmark(uid: String, bookmark: Bookmark.DefaultBookmark)
}

class DefaultBookmarkFirestoreImpl @Inject constructor() : DefaultBookmarkFirestore {

    private val firestore = Firebase.firestore

    @ExperimentalCoroutinesApi
    override fun fetchBookmarkList(uid: String) = callbackFlow {
        val listenerRegistration = try {
            firestore.defaultBookmarksRef(uid)
                .addSnapshotListener { snapshot, exception ->
                    exception?.run { throw this }

                    val entityList =
                        snapshot?.toObjects(DefaultBookmarkEntity::class.java)
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
    override suspend fun storeBookmark(uid: String, bookmark: Bookmark.DefaultBookmark) {
        firestore.defaultBookmarksRef(uid).add(bookmark)
    }

}
