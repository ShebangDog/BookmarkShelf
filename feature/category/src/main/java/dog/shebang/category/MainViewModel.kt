package dog.shebang.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dog.shebang.core.LifecycleStateFlow
import dog.shebang.core.bufferUntilStarted
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

    val lifecycleStateFlow = LifecycleStateFlow()

    data class UiModel(
        val categoryList: List<Category> = listOf()
    )

    private val mutableOnCategorySelected = MutableSharedFlow<Category>()
    val selectedCategoryFlow = mutableOnCategorySelected
        .bufferUntilStarted(lifecycleStateFlow)
        .shareIn(viewModelScope, SharingStarted.Eagerly)

    private val categoryListFlow = categoryRepository.fetchCategoryList()
        .filterIsInstance<LoadState.Loaded<List<Category>>>()
        .map { it.value }

    val uiModel = categoryListFlow
        .map { categoryList -> UiModel(categoryList = categoryList) }
        .onStart { emit(UiModel()) }
        .asLiveData()

    fun selectCategory(name: String) = viewModelScope.launch {
        val category = categoryListFlow
            .map { list -> list.firstOrNull { it.name == name } }
            .first() ?: Category.defaultCategory

        mutableOnCategorySelected.emit(category)
    }
}
