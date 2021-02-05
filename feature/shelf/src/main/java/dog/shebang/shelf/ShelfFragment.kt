package dog.shebang.shelf

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
            val bookmarkItemList = bookmarkList.map {
                DefaultPreviewItem(it) { _, url -> browse(url) }
            }

            adapter.update(bookmarkItemList)
        }
    }

    private fun browse(url: String) {
        val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        startActivity(browseIntent)
    }
}
