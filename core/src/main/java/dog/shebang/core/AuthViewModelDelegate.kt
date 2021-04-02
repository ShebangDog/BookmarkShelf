package dog.shebang.core

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Singleton

interface AuthViewModelDelegate {
    val firebaseUserInfoFlow: Flow<FirebaseUser?>
}

class DefaultAuthViewModelDelegate : AuthViewModelDelegate {

    private val firebaseAuth = Firebase.auth

    @ExperimentalCoroutinesApi
    override val firebaseUserInfoFlow: Flow<FirebaseUser?> = callbackFlow {
        val authStateListener: (FirebaseAuth) -> Unit = {
            offer(it.currentUser)
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose { firebaseAuth.removeAuthStateListener(authStateListener) }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthViewModelDelegateModule {

    @Singleton
    @Provides
    fun provideAuthViewModelDelegate(): AuthViewModelDelegate = DefaultAuthViewModelDelegate()
}
