package dog.shebang.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dog.shebang.data.repository.CategoryRepository
import dog.shebang.model.Category
import dog.shebang.model.LoadState
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    categoryRepository: CategoryRepository
) : ViewModel() {

    data class UiModel(
        val selectedCategory: Category = Category.defaultCategory,
        val categoryList: List<Category> = listOf(Category.defaultCategory)
    )

    private val mutableSelectedCategoryStateFlow = MutableStateFlow(Category.defaultCategory)
    private val selectedCategoryStateFlow: StateFlow<Category> = mutableSelectedCategoryStateFlow

    private val categoryListLiveData = categoryRepository.fetchCategoryList()
        .filterIsInstance<LoadState.Loaded<List<Category>>>()
        .map { it.value }

    val uiModel = combine(
        selectedCategoryStateFlow,
        categoryListLiveData
    ) { selectedCategory, categoryList ->

        UiModel(
            selectedCategory = selectedCategory,
            categoryList = categoryList,
        )
    }.onStart { emit(UiModel()) }.asLiveData()


    fun onCategorySelected(category: Category) {
        mutableSelectedCategoryStateFlow.value = category
    }
}
