package dog.shebang.category.di

import android.content.Context
import android.content.Intent
import dog.shebang.category.MainActivity
import dog.shebang.core.ext.Navigator
import javax.inject.Inject

class NavigatorImpl @Inject constructor() : Navigator {

    override fun navigateToMain(packageContext: Context, navigate: (Intent) -> Unit) {
        val intent = Intent(packageContext, MainActivity::class.java)

        navigate(intent)
    }
}