package dog.shebang.model

sealed class Bookmark {

    class DefaultBookmark(
        val metadata: Metadata.DefaultMetadata,
        val category: Category
    ) : Bookmark()

    class TwitterBookmark(
        val metadata: Metadata.TwitterMetadata,
        val category: Category
    ) : Bookmark()

}
