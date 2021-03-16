package dog.shebang.shelf.item

import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.xwray.groupie.viewbinding.BindableItem
import dog.shebang.model.Bookmark
import dog.shebang.shelf.R
import dog.shebang.shelf.databinding.LayoutBookmarkCardBinding

class DefaultPreviewItem(
    private val bookmark: Bookmark.DefaultBookmark,
    private val onClickListener: OnItemClickListener
) : BindableItem<LayoutBookmarkCardBinding>() {

    override fun bind(viewBinding: LayoutBookmarkCardBinding, position: Int) {
        viewBinding.apply {
            cardView.setOnClickListener { onClickListener(it, bookmark.metadata.url) }

            titleTextView.text = bookmark.metadata.title
            descriptionTextView.text = bookmark.metadata.description

            previewImageView.isVisible = bookmark.metadata.previewImageUrl != null

            Glide.with(root)
                .load(bookmark.metadata.previewImageUrl)
                .into(previewImageView)
        }
    }

    override fun getLayout(): Int = R.layout.layout_bookmark_card

    override fun initializeViewBinding(view: View): LayoutBookmarkCardBinding {
        return LayoutBookmarkCardBinding.bind(view)
    }
}
