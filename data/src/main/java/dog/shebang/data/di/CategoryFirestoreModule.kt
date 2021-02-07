package dog.shebang.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.data.firestore.CategoryFirestore
import dog.shebang.data.firestore.CategoryFirestoreImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CategoryFirestoreModule {

    @Singleton
    @Binds
    abstract fun bindCategoryFirestore(categoryFirestoreImpl: CategoryFirestoreImpl): CategoryFirestore
}