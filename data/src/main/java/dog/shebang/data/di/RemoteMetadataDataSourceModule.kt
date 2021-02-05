package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.RemoteMetadataDataSource
import dog.shebang.data.RemoteMetadataDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteMetadataDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindRemoteMetadataDataSource(
        remoteMetadataDataSourceImpl: RemoteMetadataDataSourceImpl
    ): RemoteMetadataDataSource

}