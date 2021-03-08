package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.firestore.DefaultBookmarkFirestore
import dog.shebang.data.firestore.DefaultBookmarkFirestoreImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DefaultBookmarkFirestoreModule {

    @Singleton
    @Binds
    abstract fun bindBookmarkFirestore(
        bookmarkFirestore: DefaultBookmarkFirestoreImpl
    ): DefaultBookmarkFirestore
}
