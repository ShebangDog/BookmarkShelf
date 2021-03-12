package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.datasource.remote.FirestoreDefaultBookmarkDataSource
import dog.shebang.data.datasource.remote.FirestoreDefaultBookmarkDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteFirestoreDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindRemoteFirestoreDataSource(
        remoteFirestoreDataSourceImpl: FirestoreDefaultBookmarkDataSourceImpl
    ): FirestoreDefaultBookmarkDataSource

}
