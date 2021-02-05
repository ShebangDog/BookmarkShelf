package dog.shebang.bookmarkshelf.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.bookmarkshelf.BuildConfig
import dog.shebang.env.Environment
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EnvironmentModule {

    @Singleton
    @Provides
    fun provideEnvironment(): Environment {
        return object : Environment {
            override val linkPreviewApiKey: String
                get() = BuildConfig.LINK_PREVIEW_API_KEY
        }
    }
}