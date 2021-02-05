package dog.shebang.post

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.wada811.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dog.shebang.component.MainActivity
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

        viewModel.metadataLiveData.observe(this) {
            binding.bookmarkCardLayout.apply {
                titleTextView.text = it?.title
                descriptionTextView.text = it?.description

                Glide.with(this@PostActivity)
                    .load(it?.image)
                    .into(imageView)
            }
        }

        binding.addButton.setOnClickListener {
            viewModel.storeBookmark()
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

    private fun Intent.handleIntent(): String? {
        val message = this.extras?.getString(Intent.EXTRA_TEXT)

        return when (action) {
            Intent.ACTION_SEND -> message
            else -> null
        }
    }
}