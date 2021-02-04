package dog.shebang.shelf

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wada811.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShelfFragment : Fragment(R.layout.fragment_shelf) {

    private val binding: ShelfFragment by viewBinding()
    private val viewModel: ShelfViewModel by viewModels()

}