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
            val (
                authorName,
                authorProfileUrl,
                text,
                url,
                internal
            ) = bookmark.metadata

            cardView.setOnClickListener { onClickListener(it, internal?.url ?: url) }

            authorNameTextView.text = authorName
            tweetTextView.text = text

            titleTextView.isVisible = internal?.title != null
            descriptionTextView.isVisible = internal?.description != null

            titleTextView.text = internal?.title
            descriptionTextView.text = internal?.description

            val imageUrl = internal?.let {
                it.mediaImageUrl ?: it.previewImageUrl
            }

            previewImageView.isVisible = imageUrl != null

            Glide.with(root)
                .load(imageUrl)
                .into(previewImageView)

            Glide.with(root)
                .load(authorProfileUrl)
                .circleCrop()
                .into(authorProfileImageView)
        }
    }

    override fun getLayout(): Int = R.layout.layout_twitter_card

    override fun initializeViewBinding(view: View): LayoutTwitterCardBinding {
        return LayoutTwitterCardBinding.bind(view)
    }
}
