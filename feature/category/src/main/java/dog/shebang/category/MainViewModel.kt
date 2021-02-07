package dog.shebang.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dog.shebang.data.RemoteCategoryDataSource
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    remoteCategoryDataSource: RemoteCategoryDataSource
) : ViewModel() {

    val categoryList = remoteCategoryDataSource.fetchCategoryList().asLiveData()

}
