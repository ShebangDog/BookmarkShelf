package dog.shebang.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dog.shebang.data.RemoteCategoryDataSource
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    remoteCategoryDataSource: RemoteCategoryDataSource
) : ViewModel() {

    val categoryListLiveData = remoteCategoryDataSource.fetchCategoryList().asLiveData()

}