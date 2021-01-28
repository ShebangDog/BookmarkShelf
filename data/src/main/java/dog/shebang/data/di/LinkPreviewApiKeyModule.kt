package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.api.LinkPreviewApiKey
import dog.shebang.data.api.LinkPreviewApiKeyImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LinkPreviewApiKeyModule {

    @Singleton
    @Binds
    abstract fun bindLinkPreviewApiKey(
        linkPreviewApiKeyImpl: LinkPreviewApiKeyImpl
    ): LinkPreviewApiKey

}