package dog.shebang.model

sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Failure<Nothing>(val throwable: Throwable) : Result<Nothing>()

    fun toLoadState(): LoadState<T> = when (this) {
        is Failure -> LoadState.Error(throwable)
        is Success -> LoadState.Loaded(value)
    }
}
