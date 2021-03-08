package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.repository.DefaultMetadataRepository
import dog.shebang.data.repository.MetadataRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MetadataRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMetadataRepository(
        metadataRepository: DefaultMetadataRepository
    ): MetadataRepository

}