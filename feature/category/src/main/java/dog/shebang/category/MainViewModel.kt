package dog.shebang.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dog.shebang.data.repository.CategoryRepository
import dog.shebang.model.Category
import dog.shebang.model.LoadState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

    private val categoryListFlow = categoryRepository.fetchCategoryList()
        .filterIsInstance<LoadState.Loaded<List<Category>>>()
        .map { it.value }

    val uiModel = combine(
        selectedCategoryStateFlow,
        categoryListFlow
    ) { selectedCategory, categoryList ->

        UiModel(
            selectedCategory = selectedCategory,
            categoryList = categoryList,
        )
    }.onStart { emit(UiModel()) }.asLiveData()

    fun onCategorySelected(name: String) = viewModelScope.launch {
        mutableSelectedCategoryStateFlow.value = categoryListFlow
            .map { list -> list.firstOrNull { it.name == name } ?: Category.defaultCategory }
            .first()
    }
}
