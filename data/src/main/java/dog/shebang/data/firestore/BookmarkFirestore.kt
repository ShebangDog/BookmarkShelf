package dog.shebang.data.firestore

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dog.shebang.data.firestore.entity.BookmarkEntity
import dog.shebang.data.firestore.ext.bookmarksRef
import dog.shebang.model.Bookmark
import dog.shebang.model.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface BookmarkFirestore {

    fun fetchBookmarkList(): Flow<Result<List<Bookmark>>>

    suspend fun storeBookmark(bookmark: Bookmark)
}

class BookmarkFirestoreImpl @Inject constructor() : BookmarkFirestore {

    private val firestore = Firebase.firestore

    @ExperimentalCoroutinesApi
    override fun fetchBookmarkList(): Flow<Result<List<Bookmark>>> = callbackFlow {
        val listenerRegistration = try {
            val uid = Firebase.auth.currentUser?.uid ?: throw FirebaseNotLoggedException

            firestore.bookmarksRef(uid)
                .addSnapshotListener { snapshot, exception ->
                    exception?.run { throw this }

                    val entityList = snapshot?.toObjects(BookmarkEntity::class.java)

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
    override suspend fun storeBookmark(bookmark: Bookmark) {
        val uid = FirebaseAuthentication.currentUser.filterNotNull().first().uid

        firestore.bookmarksRef(uid).add(bookmark)
    }
}
