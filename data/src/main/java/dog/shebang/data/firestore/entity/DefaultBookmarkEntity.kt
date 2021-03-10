package dog.shebang.data.firestore.entity

import dog.shebang.model.Bookmark
import dog.shebang.model.Metadata

data class DefaultBookmarkEntity(
    val metadata: DefaultMetadataEntity? = null,
    val category: CategoryEntity? = null
) {

    fun modelOrNull(): Bookmark.DefaultBookmark? = try {
        Bookmark.DefaultBookmark(
            metadata = metadata?.modelOrNull()!!,
            category = category?.modelOrNull()!!
        )
    } catch (throwable: Throwable) {
        null
    }

    data class DefaultMetadataEntity(
        val title: String? = null,
        val description: String? = null,
        val previewImageUrl: String? = null,
        val url: String? = null
    ) {

        fun modelOrNull(): Metadata.DefaultMetadata? = try {
            Metadata.DefaultMetadata(
                title = title!!,
                description = description!!,
                previewImageUrl = previewImageUrl!!,
                url = url!!
            )
        } catch (throwable: Throwable) {
            null
        }
    }

}