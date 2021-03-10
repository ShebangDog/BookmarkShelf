package dog.shebang.data

import dog.shebang.data.firestore.CategoryFirestore
import dog.shebang.model.Category
import dog.shebang.model.LoadState
import dog.shebang.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface RemoteCategoryDataSource {

    fun fetchCategoryList(): Flow<LoadState<List<Category>>>

    suspend fun saveCategory(category: Category)
}

class RemoteCategoryDataSourceImpl @Inject constructor(
    private val categoryFirestore: CategoryFirestore
) : RemoteCategoryDataSource {

    override fun fetchCategoryList(): Flow<LoadState<List<Category>>> = flow {
        val loadStateFlow = categoryFirestore.fetchCategoryList().map { result ->
            when (result) {
                is Result.Failure -> LoadState.Error(result.throwable)
                is Result.Success -> LoadState.Loaded(result.value)
            }
        }

        emitAll(loadStateFlow)
    }

    override suspend fun saveCategory(category: Category) = categoryFirestore.saveCategory(category)
}

