package dog.shebang.bookmarkshelf.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dog.shebang.bookmarkshelf.BuildConfig
import dog.shebang.env.Environment

@Module
@InstallIn(ApplicationComponent::class)
class EnvironmentModule {

    @Provides
    fun provideEnvironment(): Environment {
        return object : Environment {
            override val linkPreviewApiKey: String
                get() = BuildConfig.LINK_PREVIEW_API_KEY
        }
    }
}