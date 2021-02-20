package dog.shebang.data.firestore

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dog.shebang.data.firestore.entity.CategoryEntity
import dog.shebang.data.firestore.ext.categoriesRef
import dog.shebang.model.Category
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

interface CategoryFirestore {

    fun fetchCategoryList(): Flow<List<Category>>

    suspend fun saveCategory(category: Category)
}

class CategoryFirestoreImpl @Inject constructor() : CategoryFirestore {

    private val firestore = Firebase.firestore

    @ExperimentalCoroutinesApi
    override fun fetchCategoryList(): Flow<List<Category>> = callbackFlow {

        firestore.categoriesRef(Firebase.auth.currentUser?.uid ?: "null")
            .addSnapshotListener { snapshot, exception ->
                exception?.run { return@run }

                val entityList = snapshot?.toObjects(CategoryEntity::class.java)

                val categoryList = entityList
                    ?.mapNotNull { it.modelOrNull() }
                    ?: emptyList()

                offer(categoryList)

            }.also { awaitClose { it.remove() } }
    }

    override suspend fun saveCategory(category: Category) {

        firestore.categoriesRef(Firebase.auth.currentUser?.uid ?: "null").add(category)
    }
}
