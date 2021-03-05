package dog.shebang.data.api.entity

import dog.shebang.model.Metadata

data class MetadataEntity(
    val title: String,
    val description: String,
    val image: String,
    val url: String
) {

    fun toModel(): Metadata = Metadata(title, description, image, url)

}