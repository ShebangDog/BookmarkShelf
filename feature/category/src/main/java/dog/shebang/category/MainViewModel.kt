package dog.shebang.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dog.shebang.data.datasource.remote.RemoteCategoryDataSource
import dog.shebang.model.Category
import dog.shebang.model.LoadState
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    remoteCategoryDataSource: RemoteCategoryDataSource
) : ViewModel() {

    private val isMutableSelectedCategoryLiveData = MutableLiveData(Category.defaultCategory)
    val selectedCategoryLiveData: LiveData<Category> = isMutableSelectedCategoryLiveData

    val categoryListLiveData = liveData {
        remoteCategoryDataSource.fetchCategoryList().collect { loadState ->
            if (loadState is LoadState.Loaded) emit(loadState.value)
        }
    }

    fun onCategorySelected(category: Category) {
        isMutableSelectedCategoryLiveData.value = category
    }
}
