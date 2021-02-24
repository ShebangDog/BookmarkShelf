package dog.shebang.data.firestore

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dog.shebang.data.firestore.entity.CategoryEntity
import dog.shebang.data.firestore.ext.categoriesRef
import dog.shebang.model.Category
import dog.shebang.model.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

interface CategoryFirestore {

    fun fetchCategoryList(): Flow<Result<List<Category>>>

    suspend fun saveCategory(category: Category)
}

class CategoryFirestoreImpl @Inject constructor() : CategoryFirestore {

    private val firestore = Firebase.firestore

    @ExperimentalCoroutinesApi
    override fun fetchCategoryList(): Flow<Result<List<Category>>> = callbackFlow {
        val listenerRegistration = try {
            val uid = Firebase.auth.currentUser?.uid ?: throw FirebaseNotLoggedException

            firestore.categoriesRef(uid)
                .addSnapshotListener { snapshot, exception ->
                    exception?.run { throw this }

                    val entityList = snapshot?.toObjects(CategoryEntity::class.java)

                    val categoryList = entityList
                        ?.mapNotNull { it.modelOrNull() }
                        ?: emptyList()

                    offer(Result.Success(categoryList))

                }

        } catch (throwable: Throwable) {

            offer(Result.Failure<Nothing>(throwable))
            null
        }

        awaitClose { listenerRegistration?.remove() }
    }

    override suspend fun saveCategory(category: Category) {
        val uid = Firebase.auth.currentUser?.uid ?: return

        firestore.categoriesRef(uid).add(category)
    }
}
