package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.firestore.BookmarkFirestore
import dog.shebang.data.firestore.BookmarkFirestoreImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BookmarkFirestoreModule {

    @Singleton
    @Binds
    abstract fun bindBookmarkFirestore(bookmarkFirestore: BookmarkFirestoreImpl): BookmarkFirestore
}
