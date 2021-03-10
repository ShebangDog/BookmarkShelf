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

    fun <R> flatMap(transform: (T) -> LoadState<R>): LoadState<R> = when (this) {
        is Loaded -> transform(value)
        is Error -> Error(throwable)
        is Loading -> Loading
    }

    companion object {
        fun <T, R> ifIsLoaded(
            left: LoadState<T>,
            right: LoadState<R>,
            consumer: (T, R) -> Unit
        ) {

            left.ifIsLoaded { l ->
                right.ifIsLoaded { r ->
                    consumer(l, r)
                }
            }
        }

        fun <A, B, R> map(
            left: LoadState<A>,
            right: LoadState<B>,
            transform: (A, B) -> R
        ): LoadState<R> {

            return left.flatMap { l ->
                right.map { r ->
                    transform(l, r)
                }
            }
        }
    }

}
