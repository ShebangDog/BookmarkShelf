package dog.shebang.shelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import dog.shebang.data.RemoteFirestoreDataSource
import javax.inject.Inject

@HiltViewModel
class ShelfViewModel @Inject constructor(
    remoteFirestoreDataSource: RemoteFirestoreDataSource
) : ViewModel() {

    val bookmarkListLiveData = remoteFirestoreDataSource.fetchBookmarkList().asLiveData()
}