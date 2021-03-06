package dog.shebang.category

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
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

        binding.apply {
            val listener = OnNavigationCategorySelectedListener(
                categoryDrawerLayout,
                categorySelectorNavigationView,
                navController,
                viewModel
            )

            categorySelectorNavigationView.apply {
                setupWithNavController(navController)
                setNavigationItemSelectedListener(listener)
            }

            topAppBar.setOnClickListener {
                categoryDrawerLayout.open()
            }

            viewModel.categoryListLiveData.observe(this@MainActivity) { categoryList ->
                val selectedCategory = viewModel.selectedCategoryLiveData.value ?: return@observe
                val newCategoryList = categoryList + Category.defaultCategory

                categorySelectorNavigationView.updateMenu(newCategoryList, selectedCategory)
            }

        }
    }

    private fun NavigationView.addCategory(
        order: Int,
        category: Category,
        groupId: Int = R.id.group
    ) {

        menu.add(groupId, category.value.hashCode(), order, category.value)
    }

    private fun NavigationView.updateMenu(itemList: List<Category>, selectedCategory: Category) {

        menu.clear()
        itemList.forEachIndexed { index, category ->
            addCategory(
                itemList.size - index,
                category
            )
        }

        menu.forEach { menuItem ->
            if (selectedCategory.value == menuItem.title) {
                menuItem.isChecked = true
                menuItem.isCheckable = true

                return@forEach
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

    class OnNavigationCategorySelectedListener(
        private val drawerLayout: DrawerLayout,
        private val navigationView: NavigationView,
        private val navController: NavController,
        private val viewModel: MainViewModel
    ) : NavigationView.OnNavigationItemSelectedListener {

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            item.isCheckable = true
            item.isChecked = true

            val categoryList = viewModel.categoryListLiveData.value ?: return true
            val name = item.title.toString()
            val category = categoryList.firstOrNull { it.value == name } ?: Category.defaultCategory
            val action = ShelfFragmentDirections.shelfToShelf(
                category.value, category.color.value
            )

            viewModel.onCategorySelected(category)

            navController.navigate(action)
            drawerLayout.closeDrawer(navigationView)

            return true
        }
    }
}