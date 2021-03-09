package dog.shebang.model

import android.net.Uri

sealed class Metadata(open val url: String?) {

    data class DefaultMetadata(
        val title: String?,
        val description: String?,
        val previewImageUrl: String?,
        override val url: String
    ) : Metadata(url)

    data class TwitterMetadata(
        val authorName: String,
        val authorProfileUrl: String,
        val text: String,
        val internal: DefaultMetadata?
    ) : Metadata(internal?.url)

    sealed class Type(val value: String) {

        object Default : Type(none)
        object Twitter : Type(twitter)

        companion object {
            const val twitter = "twitter.com"
            const val none = ""

            val itemList = listOf<Type>(Twitter)

            fun parseUrl(url: String): Type {
                val host = Uri.parse(url).host ?: return Default

                itemList.forEach {
                    if (host.contains(it.value)) return it
                }

                return Default
            }

            fun urlToTweetId(url: String): String {

                val startIndex = url.lastIndexOf("/")
                val endIndex = url.lastIndexOf("?")

                return if (endIndex == -1) url.substring(startIndex + 1)
                else url.substring(startIndex + 1, endIndex)
            }

        }
    }
}
