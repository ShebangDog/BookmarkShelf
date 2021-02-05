package dog.shebang.data.entity

import dog.shebang.model.Bookmark

data class BookmarkEntity(
    val metadata: MetadataEntity? = null,
    val category: CategoryEntity? = null
) {

    fun modelOrNull(): Bookmark? {
        val elemList = listOf(metadata, category)
        if (elemList.contains(null)) return null

        return Bookmark(metadata?.modelOrNull()!!, category?.modelOrNull()!!)
    }
}