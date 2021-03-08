package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.firestore.TwitterBookmarkFirestore
import dog.shebang.data.firestore.TwitterBookmarkFirestoreImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class TwitterBookmarkFirestoreModule {

    @Binds
    @Singleton
    abstract fun bindTwitterBookmarkFirestore(
        twitterBookmarkFirestoreImpl: TwitterBookmarkFirestoreImpl
    ): TwitterBookmarkFirestore
}