package dog.shebang.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dog.shebang.data.repository.CategoryRepository
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    categoryRepository: CategoryRepository
) : ViewModel() {

    val categoryListLiveData = categoryRepository.fetchCategoryList().asLiveData()

}