package dog.shebang.data.repository

import dog.shebang.data.firestore.CategoryFirestore
import dog.shebang.data.firestore.FirebaseNotLoggedException
import dog.shebang.model.Category
import dog.shebang.model.LoadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface CategoryRepository {

    fun fetchCategoryList(uid: String?): Flow<LoadState<List<Category>>>

    suspend fun saveCategory(uid: String?, category: Category)
}

class DefaultCategoryRepository @Inject constructor(
    private val categoryFirestore: CategoryFirestore
) : CategoryRepository {

    override fun fetchCategoryList(uid: String?) = flow {
        if (uid == null) {
            emit(LoadState.Error(FirebaseNotLoggedException))
            return@flow
        }

        emitAll(
            categoryFirestore.fetchCategoryList(uid)
                .map { it.toLoadState() }
        )

    }

    override suspend fun saveCategory(uid: String?, category: Category) {
        uid ?: return

        categoryFirestore.saveCategory(uid, category)
    }
}
