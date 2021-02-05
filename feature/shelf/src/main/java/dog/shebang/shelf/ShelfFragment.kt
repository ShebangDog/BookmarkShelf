package dog.shebang.shelf

import BindableItemProvider
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wada811.viewbinding.viewBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.viewbinding.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import dog.shebang.component.databinding.LayoutBookmarkCardBinding
import dog.shebang.component.ext.setSpan
import dog.shebang.shelf.databinding.FragmentShelfBinding
import dog.shebang.shelf.item.DefaultPreviewItem
import dog.shebang.shelf.item.ItemType
import dog.shebang.shelf.item.TwitterPreviewItem

@AndroidEntryPoint
class ShelfFragment : Fragment(R.layout.fragment_shelf) {

    private val binding: FragmentShelfBinding by viewBinding()
    private val viewModel: ShelfViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<GroupieViewHolder<LayoutBookmarkCardBinding>>()

        binding.apply {
            shelfRecyclerView.setSpan(all = 32)

            shelfRecyclerView.adapter = adapter
        }

        viewModel.bookmarkListLiveData.observe(viewLifecycleOwner) { bookmarkList ->

            val bindableItemProvider: BindableItemProvider = { bookmark, listener ->
                when (Url.parseDataType(bookmark.metadata.url)) {
                    is ItemType.Twitter -> TwitterPreviewItem(bookmark, listener)
                    is ItemType.Facebook -> DefaultPreviewItem(bookmark, listener)
                    is ItemType.None -> DefaultPreviewItem(bookmark, listener)
                }
            }

            val bookmarkItemList = bookmarkList.map {
                bindableItemProvider(it) { _, url -> browse(url) }
            }

            adapter.update(bookmarkItemList)
        }
    }

    private fun browse(url: String) {
        val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        startActivity(browseIntent)
    }
}

object Url {

    fun parseDataType(url: String): ItemType {
        val host = Uri.parse(url).host ?: return ItemType.None

        ItemType.itemList.forEach {
            if (host.contains(it.type)) return it
        }

        return ItemType.None
    }
}
