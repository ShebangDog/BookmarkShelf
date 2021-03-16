package dog.shebang.shelf

import BindableItemProvider
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.wada811.viewbinding.viewBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.viewbinding.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import dog.shebang.core.databinding.LayoutBookmarkCardBinding
import dog.shebang.core.ext.setSpan
import dog.shebang.model.Bookmark
import dog.shebang.model.Category
import dog.shebang.model.Color
import dog.shebang.model.LoadState
import dog.shebang.shelf.databinding.FragmentShelfBinding
import dog.shebang.shelf.item.DefaultPreviewItem
import dog.shebang.shelf.item.TwitterPreviewItem
import javax.inject.Inject

@AndroidEntryPoint
class ShelfFragment : Fragment(R.layout.fragment_shelf) {

    private val binding: FragmentShelfBinding by viewBinding()
    private val arguments: ShelfFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ShelfViewModel.ShelfViewModelFactory
    private val viewModel: ShelfViewModel by viewModels {
        ShelfViewModel.provideFactory(
            this,
            factory,
            arguments.categoryName?.let {
                Category(it, Color.defaultColor)
            } ?: Category.defaultCategory
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<GroupieViewHolder<LayoutBookmarkCardBinding>>()

        binding.apply {
            shelfRecyclerView.setSpan(all = 32)

            shelfRecyclerView.adapter = adapter
        }

        viewModel.bookmarkListLiveData.observe(viewLifecycleOwner) { loadState ->

            val bindableItemProvider: BindableItemProvider = { bookmark, listener ->
                when (bookmark) {
                    is Bookmark.DefaultBookmark -> DefaultPreviewItem(bookmark, listener)
                    is Bookmark.TwitterBookmark -> TwitterPreviewItem(bookmark, listener)
                }
            }

            when (loadState) {
                is LoadState.Error -> {
                }
                is LoadState.Loading -> binding.progressBar.isVisible = true
                is LoadState.Loaded -> {
                    binding.progressBar.isVisible = false

                    val bookmarkList = loadState.value
                    val bookmarkItemList = bookmarkList.map {
                        bindableItemProvider(it) { _, url -> browse(url) }
                    }

                    adapter.update(bookmarkItemList)
                }
            }
        }
    }

    private fun browse(url: String) {
        val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        startActivity(browseIntent)
    }
}

