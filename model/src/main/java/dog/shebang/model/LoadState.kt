package dog.shebang.model

sealed class LoadState<out T> {
    object Loading : LoadState<Nothing>()
    class Loaded<T>(val value: T) : LoadState<T>()
    class Error(val throwable: Throwable) : LoadState<Nothing>()

    fun ifIsLoaded(consumer: (T) -> Unit) {
        if (this is Loaded) consumer(value)
    }
}
