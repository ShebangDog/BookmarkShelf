package dog.shebang.data.api.entity

import dog.shebang.model.Twitter

object TwitterEntity {

    data class Tweet(
        val data: Data,
        val includes: Includes? = null
    ) {

        data class Data(
            val id: String,
            val text: String,
            val author_id: String,
            val entities: Entities?,
            val attachments: Attachments
        ) {

            fun toModel(medias: List<Includes.Media>) = Twitter.Tweet.Data(
                authorId = author_id,
                id = id,
                text = text,
                previews = entities?.urls?.map { it.toModel() }.orEmpty(),
                medias = medias.map { it.toModel() },
            )

            data class Entities(
                val urls: List<Url>?,
            ) {

                data class Url(
                    val expanded_url: String,
                    val images: List<Image>?,
                    val title: String?,
                    val description: String?
                ) {

                    data class Image(
                        val url: String,
                        val width: Int,
                        val height: Int
                    ) {

                        fun toModel(): Twitter.Tweet.Data.Preview.Image {

                            return Twitter.Tweet.Data.Preview.Image(
                                url = url,
                                width = width,
                                height = height
                            )
                        }
                    }

                    fun toModel(): Twitter.Tweet.Data.Preview {

                        return Twitter.Tweet.Data.Preview(
                            expanded_url = expanded_url,
                            images = images?.map { it.toModel() }.orEmpty(),
                            title = title,
                            description = description
                        )
                    }
                }
            }

            data class Attachments(
                val media_keys: List<String>
            )
        }

        data class Includes(
            val media: List<Media>
        ) {

            data class Media(
                val media_key: String,
                val type: String,
                val url: String
            ) {

                fun toModel(): Twitter.Tweet.Data.Media {

                    return Twitter.Tweet.Data.Media(
                        media_key = media_key,
                        type = type,
                        url = url
                    )
                }
            }
        }

        fun toModel(): Twitter.Tweet {

            return Twitter.Tweet(
                data = data.toModel(
                    medias = includes?.media.orEmpty()
                )
            )
        }
    }

    data class UserData(
        val data: Data
    ) {

        data class Data(
            val id: String,
            val name: String,
            val username: String,
            val profile_image_url: String
        ) {

            fun toModel() = Twitter.UserData.Data(
                id = id,
                name = name,
                userName = username,
                profileImageUrl = profile_image_url
            )
        }

        fun toModel() = Twitter.UserData(
            data = data.toModel()
        )
    }

}