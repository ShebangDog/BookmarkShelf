package dog.shebang.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dog.shebang.data.RemoteCategoryDataSource
import dog.shebang.model.LoadState
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    remoteCategoryDataSource: RemoteCategoryDataSource
) : ViewModel() {

    val categoryListLiveData = liveData {
        remoteCategoryDataSource.fetchCategoryList().collect { loadState ->
            if (loadState is LoadState.Loaded) emit(loadState.value)
        }
    }
}
