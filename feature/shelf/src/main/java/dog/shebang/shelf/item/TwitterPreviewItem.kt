package dog.shebang.shelf.item

import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.xwray.groupie.viewbinding.BindableItem
import dog.shebang.model.Bookmark
import dog.shebang.shelf.R
import dog.shebang.shelf.databinding.LayoutTwitterCardBinding

class TwitterPreviewItem(
    private val bookmark: Bookmark.TwitterBookmark,
    private val onClickListener: OnItemClickListener
) : BindableItem<LayoutTwitterCardBinding>() {

    override fun bind(viewBinding: LayoutTwitterCardBinding, position: Int) {
        viewBinding.apply {
            cardView.setOnClickListener { onClickListener(it, bookmark.metadata.url) }

            authorNameTextView.text = bookmark.metadata.authorName
            tweetTextView.text = bookmark.metadata.text
            titleTextView.text = bookmark.metadata.internal?.title
            descriptionTextView.text = bookmark.metadata.internal?.description

            val imageUrl = bookmark.metadata.internal?.let {
                it.mediaImageUrl ?: it.previewImageUrl
            }

            previewImageView.isVisible = imageUrl != null

            Glide.with(root)
                .load(imageUrl)
                .into(previewImageView)

            Glide.with(root)
                .load(bookmark.metadata.authorProfileUrl)
                .circleCrop()
                .into(authorProfileImageView)
        }
    }

    override fun getLayout(): Int = R.layout.layout_twitter_card

    override fun initializeViewBinding(view: View): LayoutTwitterCardBinding {
        return LayoutTwitterCardBinding.bind(view)
    }
}
