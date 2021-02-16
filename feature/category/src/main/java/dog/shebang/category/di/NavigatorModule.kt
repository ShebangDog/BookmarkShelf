package dog.shebang.category.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dog.shebang.core.ext.Navigator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorModule {

    @Singleton
    @Binds
    abstract fun bindNavigator(navigatorImpl: NavigatorImpl): Navigator

}