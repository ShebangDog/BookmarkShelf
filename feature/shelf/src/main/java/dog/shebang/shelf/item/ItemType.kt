package dog.shebang.shelf.item

sealed class ItemType(val type: String) {
    object Twitter : ItemType(twitter)
    object Facebook : ItemType(facebook)
    object None : ItemType(none)

    companion object {
        const val twitter = "twitter.com"
        const val facebook = "facebook.com"
        const val none = ""

        val itemList = listOf(Twitter, Facebook)
    }
}
