package dog.shebang.shelf.item

import android.view.View
import com.bumptech.glide.Glide
import com.xwray.groupie.viewbinding.BindableItem
import dog.shebang.model.Bookmark
import dog.shebang.shelf.R
import dog.shebang.shelf.databinding.LayoutTwitterCardBinding

class TwitterPreviewItem(
    private val bookmark: Bookmark,
    private val onClickListener: OnItemClickListener
) : BindableItem<LayoutTwitterCardBinding>() {

    override fun bind(viewBinding: LayoutTwitterCardBinding, position: Int) {
        viewBinding.apply {
            cardView.setOnClickListener { onClickListener(it, bookmark.metadata.url) }

            titleTextView.text = bookmark.metadata.title
            descriptionTextView.text = bookmark.metadata.description

            Glide.with(root)
                .load(bookmark.metadata.image)
                .circleCrop()
                .into(imageView)
        }
    }

    override fun getLayout(): Int = R.layout.layout_twitter_card

    override fun initializeViewBinding(view: View): LayoutTwitterCardBinding {
        return LayoutTwitterCardBinding.bind(view)
    }
}
