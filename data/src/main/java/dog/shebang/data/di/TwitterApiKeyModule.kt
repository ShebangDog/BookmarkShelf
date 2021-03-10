package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.api.TwitterApiKey
import dog.shebang.data.api.TwitterApiKeyImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TwitterApiKeyModule {

    @Singleton
    @Binds
    abstract fun bindTwitterApiKey(
        twitterApiKeyImpl: TwitterApiKeyImpl
    ): TwitterApiKey

}