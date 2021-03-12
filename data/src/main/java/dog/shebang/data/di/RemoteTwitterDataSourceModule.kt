package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.datasource.remote.RemoteTwitterMetadataDataSource
import dog.shebang.data.datasource.remote.RemoteTwitterMetadataDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteTwitterDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindRemoteTwitterDataSource(
        remoteTwitterDataSourceImpl: RemoteTwitterMetadataDataSourceImpl
    ): RemoteTwitterMetadataDataSource

}