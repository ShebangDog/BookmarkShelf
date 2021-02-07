package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.RemoteCategoryDataSource
import dog.shebang.data.RemoteCategoryDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteCategoryDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindRemoteCategoryDataSource(
        remoteCategoryDataSourceImpl: RemoteCategoryDataSourceImpl
    ): RemoteCategoryDataSource
}