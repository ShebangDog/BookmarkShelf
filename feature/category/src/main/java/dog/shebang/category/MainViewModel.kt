package dog.shebang.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dog.shebang.data.repository.CategoryRepository
import dog.shebang.model.Category
import dog.shebang.model.LoadState
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    categoryRepository: CategoryRepository
) : ViewModel() {

    private val mutableSelectedCategoryLiveData = MutableLiveData(Category.defaultCategory)
    val selectedCategoryLiveData: LiveData<Category> = mutableSelectedCategoryLiveData

    val categoryListLiveData = categoryRepository.fetchCategoryList()
        .filterIsInstance<LoadState.Loaded<List<Category>>>()
        .map { it.value }
        .asLiveData()

    fun onCategorySelected(category: Category) {
        mutableSelectedCategoryLiveData.value = category
    }
}
