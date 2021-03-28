package dog.shebang.category

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wada811.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dog.shebang.category.databinding.ActivityMainBinding
import dog.shebang.data.firestore.FirebaseAuthentication
import dog.shebang.model.Category
import dog.shebang.shelf.ShelfFragmentDirections
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding()
    private val viewModel: MainViewModel by viewModels()

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.container_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        lifecycle.addObserver(viewModel.lifecycleStateFlow)

        binding.apply {
            topAppBar.setOnClickListener {
                categoryDrawerLayout.open()
            }

            lifecycleScope.launch {
                viewModel.selectedCategoryFlow.collect {
                    navigateToShelfByCategory(navController, it)
                    categoryDrawerLayout.closeDrawer(categorySelectorNavigationView)
                }
            }

            viewModel.uiModel.observe(this@MainActivity) { uiModel ->
                val newCategoryList = uiModel.categoryList + Category.defaultCategory

                categorySelectorNavigationView.updateMenu(newCategoryList)
            }

            categorySelectorNavigationView.setNavigationItemSelectedListener { item ->
                item.isCheckable = true
                item.isChecked = true

                val name = item.title.toString()

                viewModel.selectCategory(name)

                true
            }
        }
    }

    private fun NavigationView.addCategory(
        order: Int,
        category: Category,
        groupId: Int = R.id.group,
        propertyApplier: MenuItem.() -> Unit = {},
    ) {

        menu.add(groupId, category.name.hashCode(), order, category.name).apply(propertyApplier)
    }

    private fun NavigationView.updateMenu(itemList: List<Category>) {
        var checkedItem: MenuItem? = null
        menu.forEach {
            if (it.isChecked) {
                checkedItem = it
                return@forEach
            }
        }

        menu.clear()
        itemList.forEachIndexed { index, category ->
            addCategory(
                itemList.size - index,
                category
            ) {
                val isSelectedCategory = (category.name == checkedItem?.title)
                isCheckable = isSelectedCategory
                isChecked = isSelectedCategory
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {
            googleSignInClient = FirebaseAuthentication.getClient(
                this,
                getString(R.string.default_web_client_id)
            )

            showSignInIntent()
        }
    }

    private fun showSignInIntent() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(
            signInIntent,
            FirebaseAuthentication.GOOGLE_AUTH_INTENT_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FirebaseAuthentication.GOOGLE_AUTH_INTENT_REQUEST) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken ?: return

                FirebaseAuthentication.signInWithGoogle(idToken)
            } catch (e: ApiException) {

            }
        }
    }

    private fun navigateToShelfByCategory(navController: NavController, category: Category) {

        val action = ShelfFragmentDirections.shelfToShelf(
            categoryName = category.name,
            categoryColor = category.color.value
        )

        navController.navigate(action)
    }
}