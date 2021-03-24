package dog.shebang.post

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.wada811.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dog.shebang.core.ext.listener
import dog.shebang.model.*
import dog.shebang.post.databinding.ActivityPostBinding
import javax.inject.Inject

@AndroidEntryPoint
class PostActivity : AppCompatActivity(R.layout.activity_post) {

    @Inject
    lateinit var factory: PostViewModel.PostViewModelFactory

    private val binding: ActivityPostBinding by viewBinding()
    private val viewModel: PostViewModel by viewModels {
        PostViewModel.provideFactory(
            this,
            factory,
            intent?.handleIntent()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.uiModel.observe(this) { uiModel ->
            binding.apply {
                imageProgressBar.isVisible = uiModel.isLoading

                uiModel.error?.message?.also {
                    Snackbar.make(root, it, Snackbar.LENGTH_INDEFINITE).show()
                }

                uiModel.defaultMetadata?.also {
                    titleTextView.text = it.title
                    descriptionTextView.text = it.description

                    Glide.with(this@PostActivity)
                        .load(it.previewImageUrl)
                        .listener(
                            onSuccess = { previewImageView.isVisible = true },
                            onFailure = { previewImageView.isVisible = false }
                        )
                        .into(previewImageView)
                }

                uiModel.twitterMetadata?.also {
                    val imageUrl = it.internal?.let { internal ->
                        internal.mediaImageUrl ?: internal.previewImageUrl
                    }

                    authorNameTextView.text = it.authorName
                    descriptionTextView.text = it.text

                    Glide.with(this@PostActivity)
                        .load(imageUrl)
                        .listener(
                            onSuccess = { previewImageView.isVisible = true },
                            onFailure = { previewImageView.isVisible = false }
                        )
                        .into(previewImageView)

                    Glide.with(this@PostActivity)
                        .load(it.authorProfileUrl)
                        .listener(
                            onSuccess = { authorProfileImageView.isVisible = true },
                            onFailure = { authorProfileImageView.isVisible = false }
                        )
                        .circleCrop()
                        .into(authorProfileImageView)
                }

                uiModel.category.also {
                    categoryChip.text = it.name
                    categoryChip.chipBackgroundColor = ColorStateList.valueOf(it.color.value)
                }

                addButton.setOnClickListener {
                    val metadata = uiModel.defaultMetadata
                        ?: uiModel.twitterMetadata
                        ?: return@setOnClickListener

                    val category = uiModel.category

                    val bookmark = when (metadata) {
                        is Metadata.DefaultMetadata -> Bookmark.DefaultBookmark(metadata, category)
                        is Metadata.TwitterMetadata -> Bookmark.TwitterBookmark(metadata, category)
                    }

                    viewModel.storeBookmark(bookmark)
                    finish()
                }

                categoryChip.setOnClickListener {
                    showBottomSheet(
                        onChipClickListener = viewModel::setCategory,
                        onAddCategoryButtonClickListener = viewModel::saveCategory
                    )
                }
            }
        }
    }

    private fun Intent.handleIntent(): String? {
        val message = this.extras?.getString(Intent.EXTRA_TEXT)

        return when (action) {
            Intent.ACTION_SEND -> message
            else -> null
        }
    }

    private fun showBottomSheet(
        onAddCategoryButtonClickListener: (Category) -> Unit,
        onChipClickListener: (Category) -> Unit,
    ) {

        CategoryBottomSheet(
            onAddCategoryButtonClickListener,
            onChipClickListener
        ).apply {

            show(supportFragmentManager, CategoryBottomSheet.TAG)
        }
    }

}