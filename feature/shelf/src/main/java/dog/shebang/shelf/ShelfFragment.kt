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
import com.google.android.material.snackbar.Snackbar
import com.wada811.viewbinding.viewBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.viewbinding.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import dog.shebang.core.ext.setSpan
import dog.shebang.model.Bookmark
import dog.shebang.model.Category
import dog.shebang.model.Color
import dog.shebang.shelf.databinding.FragmentShelfBinding
import dog.shebang.shelf.databinding.LayoutBookmarkCardBinding
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
        val category = arguments.categoryName?.let {
            val color = Color.valueOf(arguments.categoryColor)
            Category(it, color)
        }

        ShelfViewModel.provideFactory(
            this,
            factory,
            category ?: Category.defaultCategory
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<GroupieViewHolder<LayoutBookmarkCardBinding>>()

        binding.apply {
            shelfRecyclerView.setSpan(all = 32)

            shelfRecyclerView.adapter = adapter

            viewModel.uiModel.observe(viewLifecycleOwner) { uiModel ->

                uiModel.isLoading.also {
                    progressBar.isVisible = it
                }

                uiModel.error.also {
                    if (it != null) {
                        Snackbar.make(root, it.message.orEmpty(), Snackbar.LENGTH_INDEFINITE)
                    }
                }

                uiModel.bookmarkList.also { bookmarkList ->
                    val bindableItemProvider: BindableItemProvider = { bookmark, listener ->
                        when (bookmark) {
                            is Bookmark.DefaultBookmark -> DefaultPreviewItem(bookmark, listener)
                            is Bookmark.TwitterBookmark -> TwitterPreviewItem(bookmark, listener)
                        }
                    }

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

