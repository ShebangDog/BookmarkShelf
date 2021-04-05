package dog.shebang.core

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.model.UserInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Singleton

interface AuthViewModelDelegate {

    val userInfoFlow: Flow<UserInfo?>
}

class DefaultAuthViewModelDelegate : AuthViewModelDelegate {

    private val firebaseAuth = Firebase.auth

    @ExperimentalCoroutinesApi
    private val firebaseUserFlow: Flow<UserInfo?> = callbackFlow {
        val authStateListener: (FirebaseAuth) -> Unit = {
            try {
                val userInfo = UserInfo(
                    uid = it.currentUser?.uid!!,
                    name = it.currentUser?.displayName,
                    profileIconUrl = it.currentUser?.photoUrl?.toString()
                )

                offer(userInfo)
            } catch (throwable: Throwable) {
                offer(null)
            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose { firebaseAuth.removeAuthStateListener(authStateListener) }
    }

    @ExperimentalCoroutinesApi
    override val userInfoFlow: Flow<UserInfo?> = firebaseUserFlow

}

@Module
@InstallIn(SingletonComponent::class)
object AuthViewModelDelegateModule {

    @Singleton
    @Provides
    fun provideAuthViewModelDelegate(): AuthViewModelDelegate = DefaultAuthViewModelDelegate()
}
