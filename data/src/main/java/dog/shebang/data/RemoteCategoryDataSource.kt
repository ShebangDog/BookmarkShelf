package dog.shebang.data

import dog.shebang.data.firestore.CategoryFirestore
import dog.shebang.model.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RemoteCategoryDataSource {

    fun fetchCategoryList(): Flow<List<Category>>

    suspend fun saveCategory(category: Category)
}

class RemoteCategoryDataSourceImpl @Inject constructor(
    private val categoryFirestore: CategoryFirestore
) : RemoteCategoryDataSource {

    override fun fetchCategoryList(): Flow<List<Category>> = categoryFirestore.fetchCategoryList()

    override suspend fun saveCategory(category: Category) = categoryFirestore.saveCategory(category)
}

