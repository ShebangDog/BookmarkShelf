package dog.shebang.post

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wada811.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dog.shebang.core.component.CategoryBottomSheet
import dog.shebang.core.ext.Navigator
import dog.shebang.model.Bookmark
import dog.shebang.model.Category
import dog.shebang.post.databinding.ActivityPostBinding
import javax.inject.Inject

@AndroidEntryPoint
class PostActivity : AppCompatActivity(R.layout.activity_post) {

    @Inject
    lateinit var navigator: Navigator

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

        viewModel.metadataLiveData.observe(this) {
            binding.bookmarkCardLayout.apply {
                titleTextView.text = it?.title
                descriptionTextView.text = it?.description

                Glide.with(this@PostActivity)
                    .load(it?.image)
                    .into(imageView)
            }
        }

        binding.apply {

            addButton.setOnClickListener {
                val metadata = viewModel.metadataLiveData.value ?: return@setOnClickListener
                val category = viewModel.categoryLiveData.value ?: return@setOnClickListener
                val bookmark = Bookmark(metadata, category)

                viewModel.storeBookmark(bookmark)

                navigator.navigateToMain(this@PostActivity, this@PostActivity::startActivity)
            }

            categoryChip.setOnClickListener {
                val onAddCategoryButtonClickListener: (Category) -> Unit = {
                    viewModel.saveCategory(it)
                }

                val onChipClickListener: (Category) -> Unit = {
                    viewModel.setCategory(it)
                }

                showBottomSheet(onAddCategoryButtonClickListener, onChipClickListener)
            }

            viewModel.categoryLiveData.observe(this@PostActivity) {
                categoryChip.text = it.value
                categoryChip.chipBackgroundColor = ColorStateList.valueOf(it.color.value)
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