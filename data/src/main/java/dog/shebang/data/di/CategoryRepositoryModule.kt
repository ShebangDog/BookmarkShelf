package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.repository.CategoryRepository
import dog.shebang.data.repository.DefaultCategoryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CategoryRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindCategoryRepository(
        defaultCategoryRepository: DefaultCategoryRepository
    ): CategoryRepository

}
