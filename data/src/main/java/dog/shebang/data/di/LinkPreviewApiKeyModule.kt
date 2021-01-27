package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dog.shebang.env.Environment
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class LinkPreviewApiKeyModule {

    @Singleton
    @Binds
    abstract fun bindLinkPreviewApiKey(
        linkPreviewApiKeyImpl: LinkPreviewApiKeyImpl
    ): LinkPreviewApiKey

}

interface LinkPreviewApiKey {

    val apiKey: String
}

class LinkPreviewApiKeyImpl @Inject constructor(
    private val environment: Environment
) : LinkPreviewApiKey {

    override val apiKey: String
        get() = environment.linkPreviewApiKey
}