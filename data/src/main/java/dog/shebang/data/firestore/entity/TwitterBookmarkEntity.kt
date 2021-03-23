package dog.shebang.data.firestore.entity

import dog.shebang.model.Bookmark
import dog.shebang.model.Metadata

data class TwitterBookmarkEntity(
    val metadata: TwitterMetadataEntity? = null,
    val category: CategoryEntity? = null
) {

    fun modelOrNull(): Bookmark.TwitterBookmark? = try {
        Bookmark.TwitterBookmark(
            metadata = metadata?.modelOrNull()!!,
            category = category?.modelOrNull()!!
        )
    } catch (throwable: Throwable) {
        null
    }

    data class TwitterMetadataEntity(
        val authorName: String? = null,
        val authorProfileUrl: String? = null,
        val text: String? = null,
        val url: String? = null,
        val internal: InternalMetadata? = null
    ) {

        fun modelOrNull(): Metadata.TwitterMetadata? = try {
            Metadata.TwitterMetadata(
                authorName = authorName!!,
                authorProfileUrl = authorProfileUrl!!,
                text = text!!,
                url = url!!,
                internal = Metadata.TwitterMetadata.InternalMetadata(
                    title = internal?.title,
                    description = internal?.description,
                    previewImageUrl = internal?.previewImageUrl,
                    mediaImageUrl = internal?.mediaImageUrl,
                    url = internal?.url!!
                )
            )
        } catch (throwable: Throwable) {
            null
        }

        data class InternalMetadata(
            val title: String? = null,
            val description: String? = null,
            val previewImageUrl: String? = null,
            val mediaImageUrl: String? = null,
            val url: String? = null
        )
    }
}