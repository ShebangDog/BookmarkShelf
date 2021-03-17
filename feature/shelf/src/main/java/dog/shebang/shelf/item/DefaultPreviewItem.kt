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
            val (
                title,
                description,
                previewImageUrl,
                url
            ) = bookmark.metadata

            cardView.setOnClickListener { onClickListener(it, url) }

            titleTextView.isVisible = title != null
            descriptionTextView.isVisible = description != null

            titleTextView.text = title
            descriptionTextView.text = description

            previewImageView.isVisible = previewImageUrl != null

            Glide.with(root)
                .load(previewImageUrl)
                .into(previewImageView)
        }
    }

    override fun getLayout(): Int = R.layout.layout_bookmark_card

    override fun initializeViewBinding(view: View): LayoutBookmarkCardBinding {
        return LayoutBookmarkCardBinding.bind(view)
    }
}
