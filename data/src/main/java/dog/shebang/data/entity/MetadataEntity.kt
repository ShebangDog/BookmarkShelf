package dog.shebang.data.entity

import dog.shebang.model.Metadata

data class MetadataEntity(
    val title: String? = null,
    val description: String? = null,
    val image: String? = null,
    val url: String? = null
) {

    fun modelOrNull(): Metadata? {
        val elemList = listOf(title, description, image, url)
        if (elemList.contains(null)) return null

        return Metadata(title!!, description!!, image!!, url!!)
    }
}
