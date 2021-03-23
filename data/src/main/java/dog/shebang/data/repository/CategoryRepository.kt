package dog.shebang.data.repository

import dog.shebang.data.firestore.CategoryFirestore
import dog.shebang.model.Category
import dog.shebang.model.LoadState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface CategoryRepository {

    fun fetchCategoryList(): Flow<LoadState<List<Category>>>

    suspend fun saveCategory(category: Category)
}

class DefaultCategoryRepository @Inject constructor(
    private val categoryFirestore: CategoryFirestore
) : CategoryRepository {

    override fun fetchCategoryList(): Flow<LoadState<List<Category>>> {
        return categoryFirestore.fetchCategoryList()
            .map { it.toLoadState() }

    }

    override suspend fun saveCategory(category: Category) {

        categoryFirestore.saveCategory(category)
    }
}
