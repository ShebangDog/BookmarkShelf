package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.repository.BookmarkRepository
import dog.shebang.data.repository.DefaultBookmarkRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BookmarkRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindBookmarkRepository(
        bookmarkRepository: DefaultBookmarkRepository
    ): BookmarkRepository
}