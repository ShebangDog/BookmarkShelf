package dog.shebang.post

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.wada811.viewbinding.viewBinding
import dog.shebang.post.databinding.FragmentPostBinding

class PostFragment : Fragment(R.layout.fragment_post) {

    private val binding: FragmentPostBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}