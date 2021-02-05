package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.RemoteFirestoreDataSource
import dog.shebang.data.RemoteFirestoreDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteFirestoreDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindRemoteFirestoreDataSource(
        remoteFirestoreDataSourceImpl: RemoteFirestoreDataSourceImpl
    ): RemoteFirestoreDataSource

}
