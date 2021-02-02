package dog.shebang.data.firestore

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dog.shebang.data.firestore.ext.bookmarksRef
import dog.shebang.env.Environment
import dog.shebang.model.Bookmark
import dog.shebang.model.Category
import dog.shebang.model.Metadata
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

interface BookmarkFirestore {

    fun fetchBookmark(): Flow<Bookmark>

    suspend fun storeBookmark(bookmark: Bookmark)
}

class BookmarkFirestoreImpl @Inject constructor(
    private val firestoreUserId: FirestoreUserId
) : BookmarkFirestore {

    private val firestore = Firebase.firestore

    @ExperimentalCoroutinesApi
    override fun fetchBookmark(): Flow<Bookmark> = callbackFlow {
        firestore.bookmarksRef(Firebase.auth.currentUser?.uid ?: "null")
            .addSnapshotListener { snapshot, exception ->
                exception?.run { return@run }

                offer(
                    Bookmark(
                        Metadata(
                            "マルチモジュール アプリで Dagger を使用する Android デベロッパー Android Developers",
                            "description",
                            "https://developer.android.com/images/social/android-developers.png",
                            "https://developer.android.com/training/dependency-injection/dagger-multi-module?hl=ja"
                        ),
                        Category("category")
                    )
                )
            }.also { awaitClose { it.remove() } }
    }

    override suspend fun storeBookmark(bookmark: Bookmark) {
        val uid = FirebaseAuthentication.currentUser.filterNotNull().first().uid
        Log.d(TAG, "storeBookmark: ${uid}")
        firestore.bookmarksRef(uid)
            .add(bookmark)
            .addOnCompleteListener {
                Log.d(TAG, "storeBookmark: complete")
            }
            .addOnFailureListener {
                Log.d(TAG, "storeBookmark: ${it.message} error")
            }
    }
}

interface FirestoreUserId {

    val userId: String
}

class FirestoreUserIdImpl @Inject constructor(
    private val environment: Environment
) : FirestoreUserId {

    override val userId: String
        get() = environment.userId
}
