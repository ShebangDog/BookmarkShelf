package dog.shebang.model

sealed class LoadState<out T> {
    object Loading : LoadState<Nothing>()
    class Loaded<T>(val value: T) : LoadState<T>()
    class Error(val throwable: Throwable) : LoadState<Nothing>()

    fun ifIsLoaded(consumer: (T) -> Unit) {
        if (this is Loaded) consumer(value)
    }

    fun <R> map(transform: (T) -> R): LoadState<R> = when (this) {
        is Error -> Error(throwable)
        is Loading -> Loading
        is Loaded -> Loaded(transform(value))
    }

}
