package dog.shebang.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.auth.AuthViewModelDelegate
import dog.shebang.data.auth.DefaultAuthViewModelDelegate
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthViewModelDelegateModule {

    @Singleton
    @Provides
    fun provideAuthViewModelDelegate(): AuthViewModelDelegate = DefaultAuthViewModelDelegate()
}
