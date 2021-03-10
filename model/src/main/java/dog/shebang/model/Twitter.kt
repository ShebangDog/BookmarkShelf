package dog.shebang.model

object Twitter {

    data class Tweet(
        val data: Data,
    ) {

        data class Data(
            val authorId: String,
            val id: String,
            val text: String,
            val previews: List<Preview>,
            val medias: List<Media>
        ) {

            data class Preview(
                val expanded_url: String,
                val images: List<Image>,
                val title: String?,
                val description: String?
            ) {

                data class Image(
                    val url: String,
                    val width: Int,
                    val height: Int
                )
            }

            data class Media(
                val media_key: String,
                val type: String,
                val url: String
            )
        }

    }

    data class UserData(
        val data: Data
    ) {

        data class Data(
            val id: String,
            val name: String,
            val userName: String,
            val profileImageUrl: String
        )
    }

}