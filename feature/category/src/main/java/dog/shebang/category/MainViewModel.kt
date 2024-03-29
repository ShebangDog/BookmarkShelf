package dog.shebang.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dog.shebang.core.LifecycleStateFlow
import dog.shebang.core.bufferUntilStarted
import dog.shebang.data.auth.AuthViewModelDelegate
import dog.shebang.data.repository.CategoryRepository
import dog.shebang.model.Category
import dog.shebang.model.LoadState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    authViewModelDelegate: AuthViewModelDelegate
) : ViewModel(), AuthViewModelDelegate by authViewModelDelegate {

    val lifecycleStateFlow = LifecycleStateFlow()

    data class UiModel(
        val signInState: SignInState? = null,
        val profile: Profile? = null,
        val categoryList: List<Category> = listOf()
    )

    data class Profile(
        val name: String?,
        val iconUrl: String?
    )

    sealed class SignInState {
        object SignIn : SignInState()
        data class SignOut(val message: String) : SignInState()
    }

    private val mutableCategoryListFlow =
        MutableStateFlow<LoadState<List<Category>>>(LoadState.Loading)

    private val mutableOnCategorySelected = MutableSharedFlow<Category>()
    val selectedCategoryFlow = mutableOnCategorySelected
        .bufferUntilStarted(lifecycleStateFlow)
        .shareIn(viewModelScope, SharingStarted.Eagerly)

    private val categoryListFlow = mutableCategoryListFlow

    val userState = userInfoFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val uiModel = combine(
        userState,
        categoryListFlow
    ) { user, loadState ->

        val categoryList = if (loadState is LoadState.Loaded) loadState.value else emptyList()

        val signInState = when (loadState) {
            is LoadState.Loading -> null
            is LoadState.Loaded -> SignInState.SignIn
            is LoadState.Error -> SignInState.SignOut(loadState.throwable.message.orEmpty())
        }

        val profile = Profile(
            name = user?.name,
            iconUrl = user?.profileIconUrl
        )

        UiModel(
            signInState = signInState,
            categoryList = categoryList,
            profile = profile
        )
    }.onStart {
        emit(UiModel())
    }.asLiveData()

    fun selectCategory(name: String) = viewModelScope.launch {
        val category = categoryListFlow
            .map { if (it is LoadState.Loaded) it.value else null }
            .map { list -> list?.firstOrNull { it.name == name } }
            .first() ?: Category.defaultCategory

        mutableOnCategorySelected.emit(category)
    }

    fun updateUserData(uid: String?) = viewModelScope.launch {
        mutableCategoryListFlow.emitAll(
            categoryRepository.fetchCategoryList(uid)
        )
    }
}
