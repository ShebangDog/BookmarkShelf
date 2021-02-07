package dog.shebang.core.ext

import android.content.Context
import android.content.Intent

interface Navigator {

    fun navigateToMain(packageContext: Context, navigate: (Intent) -> Unit)
}