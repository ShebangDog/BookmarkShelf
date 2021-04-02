package dog.shebang.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dog.shebang.core.AuthViewModelDelegate
import dog.shebang.data.repository.CategoryRepository
import dog.shebang.model.Category
import dog.shebang.model.LoadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    authViewModelDelegate: AuthViewModelDelegate
) : ViewModel(), AuthViewModelDelegate by authViewModelDelegate {

    val currentFirebaseUserState = firebaseUserInfoFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val mutableCategoryListStateFlow =
        MutableStateFlow<LoadState<List<Category>>>(LoadState.Loading)

    fun updateUserData(uid: String?) = viewModelScope.launch {
        mutableCategoryListStateFlow.emitAll(
            categoryRepository.fetchCategoryList(uid)
        )
    }

    val categoryListLiveData = mutableCategoryListStateFlow.asLiveData()

}